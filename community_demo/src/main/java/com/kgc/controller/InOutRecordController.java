package com.kgc.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.configuration.ApiConfiguration;
import com.kgc.entity.Community;
import com.kgc.entity.InOutRecord;
import com.kgc.entity.Personinfo;
import com.kgc.form.InOutForm;
import com.kgc.form.InoutFaceForm;
import com.kgc.mapper.InOutRecordMapper;
import com.kgc.service.CommunityService;
import com.kgc.service.InOutRecordService;
import com.kgc.service.PersoninfoService;
import com.kgc.util.Base64Util;
import com.kgc.util.FaceApi;
import com.kgc.util.Result;
import com.kgc.util.RootResp;
import com.kgc.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-08
 */
@RestController
@RequestMapping("/sys/inOut")
public class InOutRecordController {

    @Autowired
    private InOutRecordService inOutRecordService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private PersoninfoService personinfoService;
    @Value("${upload.face}")
    private String face;
    @Value("${upload.urlPrefix}")
    private String urlPrefix;
    @Autowired(required = false)
    private InOutRecordMapper inOutRecordMapper;
    @Autowired
    private ApiConfiguration apiConfiguration;

    @GetMapping("/chart")
    public Result chart(){
        Map content = this.inOutRecordService.chart();
        return Result.ok().put("data",content);
    }

    @GetMapping("/communityList")
    public Result communityList(){
        List<Community> list = this.communityService.list();
        if(list == null) return Result.error("小区不存在");
        return Result.ok().put("data",list);
    }

    @LogAnnotation("添加人脸识别")
    @PostMapping("/add")
    public Result add(@RequestBody InoutFaceForm inoutFaceForm){
        //调用腾讯AI接口
//        FaceApi faceApi = new FaceApi();
//        RootResp resp = faceApi.searchPersonsReturnsByGroup(apiConfiguration, inoutFaceForm.getFileBase64());
//        String msg = "";
//        JSONObject personInfo = null;
//        if(resp.getRet() == 0) {
//        查询成功
//            JSONObject object = JSONObject.parseObject(resp.getData().toString());
//            JSONArray resultsReturnsByGroup = object.getJSONArray("ResultsReturnsByGroup");
//            JSONObject returnsByGroupJSONObject = resultsReturnsByGroup.getJSONObject(0);
//            JSONArray groupCandidates = returnsByGroupJSONObject.getJSONArray("GroupCandidates");
//            JSONObject groupCandidatesJSONObject = groupCandidates.getJSONObject(0);
//            JSONArray candidates = groupCandidatesJSONObject.getJSONArray("Candidates");
//            //返回多个人员，匹配数据库人员信息
//            String personId ="";
//            String faceId = "";
//            String personName = "";
//            String faceUrl = "";
//            long pid = 0;
//            Personinfo p = null, p1 = null;
//            for(int i = 0;i < candidates.size();i++) {
//                candidates.getJSONObject(i);
//                personId = personInfo.getString("PersonId");
//                faceId = personInfo.getString("FaceId");
//                personName = personInfo.getString("PersonName");
//                personId = personId.substring(5);
//                pid = Integer.parseInt(personId);
//                p = personinfoService.getById(pid);
//                if(p == null)
//                    continue;
//                else
//                    p1 = p;
//                faceUrl = p.getFaceUrl();
//                if(faceUrl == null || faceUrl.equals("")){
//                    continue;
//                }
//                faceUrl = faceUrl.substring(faceUrl.lastIndexOf("/")+1,faceUrl.lastIndexOf("."));
//                if(faceId.equals(faceUrl)) {
//                    break;
//                }
//            }
//            if(p==null) {
//                return Result.ok().put("data","人员信息不存在");
//            }
//            if(inoutFaceForm.getCommunityId() != p.getCommunityId()) {
//                return Result.ok().put("data","对不起，你不是本小区居民，请与系统管理员联系。");
//            }
//            InOutRecord inoutrecord = new InOutRecord();
//            inoutrecord.setCommunityId(p.getCommunityId());
//            inoutrecord.setPersonId(p.getPersonId());
//            try {
//                //保存图片
//                String newFileName = UUID.randomUUID()+"." + inoutFaceForm.getExtName();
//                String fileName = face + newFileName;
//                Base64Util.decoderBase64File(inoutFaceForm.getFileBase64(),fileName);
//                String basePath = urlPrefix + "villegePic/face/" + newFileName;
//                //查找系统中是否有该人员的出入场信息
//                //查询条件是out_time=null 如果没有out_time就是进小区 有out_time就是出小区
//                InOutRecord inoutrecord1 = this.inOutRecordMapper.getInoutrecord(inoutrecord);
//                //进入小区
//                if(inoutrecord1 == null) {
//                    inoutrecord.setInPic(basePath);
//                    this.inOutRecordMapper.insert(inoutrecord);
//                    return Result.ok().put("status", "success").put("data", "【"+p.getUserName() + "】进入小区");
//                    //离开小区
//                } else {
//                    inoutrecord1.setOutPic(basePath);
//                    this.inOutRecordMapper.updateById(inoutrecord1);
//                    return Result.ok().put("status", "success").put("data", "【"+p.getUserName() + "】离开小区");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else{
//            msg = "人脸识别失败,错误码=" + resp.getRet() + "," + resp.getMsg();
//        }
//        return Result.ok().put("data",msg);

        //模拟人脸识别
        String fileBase64 = inoutFaceForm.getFileBase64();
        String faceBase = fileBase64.substring(0, 60);
        //如果不是头像
        if(faceBase.equals("iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAYAAAA10dzkAAAAAXNSR0IArs4c")) {
            return Result.ok().put("status", "fail").put("data", "人脸识别失败");
        }
        QueryWrapper<Personinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("face_base", faceBase);
        Personinfo personinfo = this.personinfoService.getOne(queryWrapper);

        if(personinfo == null) {
            return Result.ok().put("status", "fail").put("data", "人员信息不存在");
        }
        if(inoutFaceForm.getCommunityId() != personinfo.getCommunityId()) {
            return Result.ok().put("status", "fail").put("data", "对不起，你不是本小区居民，请与系统管理员联系");
        }
        InOutRecord inoutrecord = new InOutRecord();
        inoutrecord.setCommunityId(personinfo.getCommunityId());
        inoutrecord.setPersonId(personinfo.getPersonId());
        try {
            //保存图片
            String newFileName = UUID.randomUUID()+"." + inoutFaceForm.getExtName();
            String fileName = face + newFileName;
            Base64Util.decoderBase64File(fileBase64,fileName);
            String basePath = urlPrefix + "community/upload/face/" + newFileName;
            //查找系统中是否有该人员的出入场信息
            InOutRecord inoutrecord1 = this.inOutRecordMapper.getInoutrecord(inoutrecord);
            //进入小区
            if(inoutrecord1 == null) {
                inoutrecord.setInPic(basePath);
                this.inOutRecordMapper.insert(inoutrecord);
                return Result.ok().put("status", "success").put("data", "【"+personinfo.getUserName() + "】进入小区");
                //离开小区
            } else {
                inoutrecord1.setOutPic(basePath);
                this.inOutRecordMapper.updateById(inoutrecord1);
                return Result.ok().put("status", "success").put("data", "【"+personinfo.getUserName() + "】离开小区");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @GetMapping("/list")
    public Result list(InOutForm inOutForm){
        //get不能加注解
        PageVo pageVo = this.inOutRecordService.inOutRecordList(inOutForm);
        Map<String,Object> map = new HashMap<>();
        map.put("pageList",pageVo);
        return Result.ok().put("data",map);

    }

}

