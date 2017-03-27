package com.gdky.restfull.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.service.UploadService;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX)
public class FileUploadController {
	
	@Autowired
	private UploadService uploadService;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> handleFileUpload(
			@RequestParam("file") MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename();
			String ext = FilenameUtils.getExtension(name);
			String uploadDir = "/xpian/";
			File path = new File(Constants.UPLOAD_LOCATION + uploadDir);
			if (!path.exists()) {
				path.mkdir();
			}
			String filename = uploadDir
					+ Hashing.crc32().hashBytes(file.getBytes()) + "." + ext;
			File to = new File(Constants.UPLOAD_LOCATION + filename);
			ResponseMessage rm = new ResponseMessage(
					ResponseMessage.Type.success, "201", filename);
			try {
				Files.write(file.getBytes(), to);
				return new ResponseEntity<>(rm, HttpStatus.CREATED);
			} catch (Exception e) {
				rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
						"上传失败:" + e.getMessage());
				return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
			}
		} else {
			ResponseMessage rm = new ResponseMessage(
					ResponseMessage.Type.danger, "400", "文件为空");
			return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/upload/{folderName}", method = RequestMethod.POST)
	public ResponseEntity<?> handleMuildFileUpload(@PathVariable(value = "folderName") String folderName,
			@RequestParam("file") MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename();
			String ext = FilenameUtils.getExtension(name);
			String uploadDir = "/"+folderName+"/";
			File path = new File(Constants.UPLOAD_LOCATION + uploadDir);
			if (!path.exists()) {
				path.mkdir();
			}
			String filename = Constants.UPLOAD_LOCATION + uploadDir
					+ Hashing.crc32().hashBytes(file.getBytes()) + "." + ext;
			File to = new File(filename);
			ResponseMessage rm = new ResponseMessage(
					ResponseMessage.Type.success, "201", filename);
			try {
				Files.write(file.getBytes(), to);
				return new ResponseEntity<>(rm, HttpStatus.CREATED);
			} catch (Exception e) {
				rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
						"上传失败:" + e.getMessage());
				return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
			}
		} else {
			ResponseMessage rm = new ResponseMessage(
					ResponseMessage.Type.danger, "400", "文件为空");
			return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * 单个文件删除
	 * @param folderName
	 * @param file
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/delfile", method = RequestMethod.DELETE)
	public ResponseEntity<?> handleDelFile(@RequestBody Object url) throws IOException {
			String uploadDir = url.toString();
			File path = new File(uploadDir);
			ResponseMessage rm = new ResponseMessage(
					ResponseMessage.Type.success, "201", "文件删除成功");
			if (path.isFile() && path.exists()) {
				try {
					path.delete();
					return new ResponseEntity<>(rm, HttpStatus.OK);
				} catch (Exception e) {
					rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
							"删除失败:" + e.getMessage());
					return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
				}
			}else {
				rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
						"文件不存在");
					return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
				}
	}
	/**
	 * 插入数据库
	 * @param fj
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertFile", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> insertFile(
			@RequestBody Map<String,Object> fj)throws Exception {
		ResponseMessage rm = ResponseMessage.success("更新成功");
		try {
			uploadService.insertfile(fj);
		} catch (Exception e) {
			rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
					"更新失败:" + e.getMessage());
			return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	/**
	 * 置有效标志为无效
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upadteDelFile", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> deleteFile(
			@RequestBody String url)throws Exception {
		ResponseMessage rm = ResponseMessage.success("删除成功");
		try {
			uploadService.delfile(url);
		} catch (Exception e) {
			rm = new ResponseMessage(ResponseMessage.Type.danger, "400",
					"删除失败:" + e.getMessage());
			return new ResponseEntity<>(rm, HttpStatus.BAD_REQUEST);
			
		}
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	@RequestMapping(value = "/getFileName", method = { RequestMethod.GET })
	public ResponseEntity<?> getFileName(
			@RequestParam(value="where") String where) throws Exception  {
		Map<String,Object> rm =new HashMap<>();
		try {
			String rep=uploadService.getFileName(where);
			rm.put("rep", rep);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<>(rm,HttpStatus.OK);

	}
}
