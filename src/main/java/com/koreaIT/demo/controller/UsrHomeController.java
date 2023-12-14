package com.koreaIT.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreaIT.demo.service.FileService;
import com.koreaIT.demo.service.MemberService;
import com.koreaIT.demo.util.Util;
import com.koreaIT.demo.vo.FileVO;
import com.koreaIT.demo.vo.Rq;

@Controller
public class UsrHomeController {

	private FileService fileService;
	private MemberService memberService;
	private Rq rq;
	
	public UsrHomeController(FileService fileService, MemberService memberService, Rq rq) {
		this.fileService = fileService;
		this.memberService = memberService;
		this.rq = rq;
	}
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		model.addAttribute("member", memberService.getMemberById(rq.getLoginedMemberId()));
		return "usr/home/main";
	}
	
	@RequestMapping("/")
	public String showRoot() {
		return "redirect:/usr/home/main";
	}
	
	@RequestMapping("/usr/home/popUp")
	public String popUp() {
		return "usr/home/popUp";
	}
	
	@RequestMapping("/usr/home/apiTest")
	public String apiTest() {
		return "usr/home/apiTest";
	}
	
	@RequestMapping("/usr/home/apiTest2")
	public String apiTest2() {
		return "usr/home/apiTest2";
	}
	
	@RequestMapping("/usr/home/upload")
	@ResponseBody
	public String uploadFile(MultipartFile file) {

		try {
			fileService.saveFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Util.jsReplace("파일 업로드 성공", "/");
	}

	@RequestMapping("/usr/home/view")
	public String view(Model model) {		

		List<FileVO> files = fileService.getFiles();

		model.addAttribute("files", files);

		return "usr/home/view";
	}

	@RequestMapping("/usr/home/file/{fileId}")
	@ResponseBody
	public Resource downloadImage(@PathVariable("fileId") int id, Model model) throws IOException {

		FileVO fileVo = fileService.getFileById(id);

		return new UrlResource("file:" + fileVo.getSavedPath()); 
	}
}
