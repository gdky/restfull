package gov.gdgs.zs.service;

import gov.gdgs.zs.dao.YwglDao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gdky.restfull.configuration.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class BarcodeEncoder {


	public void encodeCode128(String contents) {
		int width = 660;
		int height = 190;
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.CODE_128, width, height, null);
			Path path = Paths
					.get(Constants.BARCODE_LOCATION, contents + ".png");
			Path parentPath = path.getParent();
			if (!Files.exists(parentPath)) {
				Files.createDirectories(parentPath);
			}
			;

			MatrixToImageWriter.writeToPath(bitMatrix, "png", path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reGenGroupBarcode(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			encodeCode128((String) (list.get(i)).get("bbhm"));
			System.out.println("总数:" + list.size() + "  已处理：" + i);
		}
	}

	public static void main(String[] args) { 
		String bbhm = "123456789";
	        BarcodeEncoder handler = new BarcodeEncoder();  
	        handler.encodeCode128(bbhm);
	  
	    }}
