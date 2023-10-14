package com.kgc.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.configuration.ApiConfiguration;
import com.kgc.entity.Community;
import com.kgc.entity.Personinfo;
import com.kgc.entity.SysUser;
import com.kgc.form.PersonFaceForm;
import com.kgc.form.PersonListForm;
import com.kgc.service.CommunityService;
import com.kgc.service.PersoninfoService;
import com.kgc.util.*;

import com.kgc.vo.PageVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/sys/person")
public class PersoninfoController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private PersoninfoService personinfoService;

    @Autowired
    private ApiConfiguration apiConfiguration;

    @Value("${upload.face}")
    private String face;
    @Value("${upload.urlPrefix}")
    private String urlPrefix;
    @Value("${upload.excel}")
    private String excel;

    @GetMapping("/list")
    public Result list( PersonListForm personListForm){
        Map<String, Object> map = new HashMap<>();
        map.put("communityList", this.communityService.list());
        map.put("pageList",this.personinfoService.personList(personListForm));
        return Result.ok().put("data",map);

    }


    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        if(id == 0){
            Map<String, List> map = new HashMap<>();
            map.put("communityList",this.communityService.list());
            return Result.ok().put("data",map);
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("communityList",this.communityService.list());
            map.put("person", this.personinfoService.getById(id));
            return Result.ok().put("data",map);
        }
    }


    @LogAnnotation("添加居民")
    @PostMapping("/add")
    public Result add(@RequestBody Personinfo personinfo, HttpSession session){
        SysUser user = (SysUser) session.getAttribute("user");
        personinfo.setCreater(user.getUsername());
        boolean save = this.personinfoService.save(personinfo);
        if(!save) return Result.error("居民添加失败");
        return Result.ok();
    }


    @LogAnnotation("编辑居民")
    @PutMapping("/edit")
    public Result edit(@RequestBody Personinfo personinfo){
        boolean update = this.personinfoService.updateById(personinfo);
        if(!update) return Result.error("居民编辑失败");
        return Result.ok();
    }

    @LogAnnotation("删除居民")
    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] ids){
        boolean remove = this.personinfoService.removeByIds(Arrays.asList(ids));
        if(!remove) return Result.error("居民删除失败");
        return Result.ok();
    }

    @LogAnnotation("人脸采集")
    @PostMapping("/addPerson")
    public Result addPerson(@RequestBody PersonFaceForm personFaceForm){
        Personinfo personinfo = this.personinfoService.getById(personFaceForm.getPersonId());
        if(personinfo == null){
            return Result.error("居民不存在");
        }
        if(personinfo.getState() == 2){
            return Result.error("人脸识别已通过，不需要重复识别");
        }
        if(personFaceForm.getFileBase64() == null || personFaceForm.getFileBase64().equals("")){
            return Result.error("请上传base64编码的图片");
        }

        //调用腾讯API人脸识别
//        String faceId_ten = newPerson(personFaceForm,personinfo.getUserName());
//        if(faceId_ten == null){
//            return Result.error("人脸识别失败");
//        }
//        if (faceId_ten != null) {
//            //生成头像访问路径
//            String filename = faceId_ten + "." + personFaceForm.getExtName();
//            String faceUrl = urlPrefix + "villegePic/face/" + filename;
//            personinfo.setFaceUrl(faceUrl);
//            personinfo.setState(2);
//            //更新人脸识别状态及图片地址
//            this.personinfoService.updateById(personinfo);
//            return Result.ok();
//        }
        //模拟人脸识别
        if(apiConfiguration.isUsed()){
            //生成模拟id
            String faceId = RandomUtil.getBitRandom();
            String faceBase = personFaceForm.getFileBase64().substring(0, 60);
            //如果不是头像 下面的hash值代表黑屏
            if(faceBase.equals("iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAYAAAA10dzkAAAAAXNSR0IArs4c")) {
                return Result.error("人脸识别失败");
            }
            //存储头像
            String filename = faceId + "." + personFaceForm.getExtName();
            String savePath = face + filename;
            try {
                Base64Util.decoderBase64File(personFaceForm.getFileBase64(), savePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //生成头像访问路径
            String faceUrl = urlPrefix + "community/upload/face/" + filename;
            personinfo.setFaceUrl(faceUrl);
            personinfo.setState(2);
            personinfo.setFaceBase(faceBase);
            //更新人脸识别状态及图片地址
            this.personinfoService.updateById(personinfo);
            return Result.ok();
        }


        return Result.error("未开启人脸识别");

    }


    @GetMapping("/exportExcel")
    public Result exportExcel(PersonListForm personListForm){
        PageVo pageVo = this.personinfoService.personList(personListForm);
        //获取所有记录
        List list = pageVo.getList();
        String path = excel;
        path = ExcelUtil.ExpPersonInfo(list,path);
        return Result.ok().put("data",path);



    }

    @PostMapping("/excelUpload")
    public Result excelUpload(@RequestParam("uploadExcel") MultipartFile file) throws Exception {
        //RequestParam在get方法中是url 地址栏 ？uploadExcel 这样传递参数
        //在post方法中 用来提取表单中的数据，包括文件， 这时候不会出现在url中而是出现在方法体中
        //uploadExcel应该是表单中<input type="file">标签的name属性值
        if(file.getOriginalFilename().equals("")){
            return Result.error("没有选中要上传的文件");
        }else {
            String picName = UUID.randomUUID().toString();
            String oriName = file.getOriginalFilename();
            String extName = oriName.substring(oriName.lastIndexOf("."));
            String newFileName = picName + extName;
            File targetFile = new File(excel, newFileName);
            // 保存文件
            file.transferTo(targetFile);
            return Result.ok().put("data",newFileName);
        }

    }

    @LogAnnotation("导入数据")
    @PostMapping("/parsefile/{fileName}")
    public Result parsefile(@PathVariable("fileName") String fileName, HttpSession session){
        SysUser user = (SysUser)session.getAttribute("user");
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            String basePath = excel + fileName;
            fs = new POIFSFileSystem(new FileInputStream(basePath));
            wb = new HSSFWorkbook(fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        Object[][] data = null;
        int r = sheet.getLastRowNum()+1;
        int c = sheet.getRow(0).getLastCellNum();
        int headRow = 2;
        data = new Object[r - headRow][c];
        for (int i = headRow; i < r; i++) {
            HSSFRow row = sheet.getRow(i);
            for (int j = 0; j < c; j++) {
                HSSFCell cell = null;
                try {
                    cell = row.getCell(j);
                    try {
                        cell = row.getCell(j);
                        DataFormatter dataFormater = new DataFormatter();
                        String a = dataFormater.formatCellValue(cell);
                        data[i - headRow][j] = a;
                    } catch (Exception e) {
                        data[i-headRow][j] = "";
                        if(j==0){
                            try {
                                double d = cell.getNumericCellValue();
                                data[i - headRow][j] = (int)d + "";
                            }catch(Exception ex){
                                data[i-headRow][j] = "";
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("i="+i+";j="+j+":"+e.getMessage());
                }
            }
        }

        int row = data.length;
        int col = 0;
        String errinfo = "";
        headRow = 3;
        String[] stitle={"ID","小区名称","所属楼栋","房号","姓名","性别","手机号码","居住性质","状态","备注"};
        errinfo = "";
        for (int i = 0; i < row; i++) {
            Personinfo single = new Personinfo();
            single.setPersonId(0);
            single.setState(1);
            single.setFaceUrl("");
            try {
                col=1;
                String communityName = data[i][col++].toString();
                QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("community_name", communityName);
                Community community = this.communityService.getOne(queryWrapper);
                if( community == null){
                    errinfo += "Excel文件第" + (i + headRow) + "行小区名称不存在！";
                    return Result.ok().put("status", "fail").put("data", errinfo);
                }
                single.setCommunityId(community.getCommunityId());
                single.setTermName(data[i][col++].toString());
                single.setHouseNo(data[i][col++].toString());
                single.setUserName(data[i][col++].toString());
                single.setSex(data[i][col++].toString());
                single.setMobile(data[i][col++].toString());
                single.setPersonType(data[i][col++].toString());
                single.setRemark(data[i][col++].toString());
                single.setCreater(user.getUsername());
//                single.setCreateTime(new Date());
                this.personinfoService.save(single);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.ok().put("status", "success").put("data","数据导入完成！");
    }

    private String newPerson(PersonFaceForm vo,String personName) {
        String faceId = null;
        String faceBase64 = vo.getFileBase64();
        String extname = vo.getExtName();
        String personId = vo.getPersonId()+"";
        String savePath = face;
        if (faceBase64!=null && !faceBase64.equals("")) {
            FaceApi faceApi = new FaceApi();
            RootResp resp = faceApi.newperson(apiConfiguration, personId, personName, faceBase64);
            if(resp.getRet()==0) {
                JSONObject data = JSON.parseObject(resp.getData().toString());
                faceId = data.getString("FaceId");
                if(faceId!=null) {
                    String filename = faceId + "." + extname;
                    savePath += filename;
                    try {
                        Base64Util.decoderBase64File(faceBase64, savePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                return faceId;
            }
        }
        return faceId;
    }
}

