package com.cafe.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.JWT.JwtFilter;
import com.cafe.POJO.Bill;
import com.cafe.contants.CafeContants;
import com.cafe.dao.BillDao;
import com.cafe.service.BillService;
import com.cafe.utils.CafeUtils;
import com.google.gson.JsonArray;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private BillDao billDao;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			String fileName;
			if (validateRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
					fileName = (String) requestMap.get("uuid");
				} else {
					fileName = CafeUtils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}

				String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: "
						+ requestMap.get("contactNumber") + "\n" + "Email: " + requestMap.get("email") + "\n"
						+ "Payment Method: " + requestMap.get("paymentMethod");

				Document document = new Document();
				PdfWriter.getInstance(document,
						new FileOutputStream(CafeContants.STORE_LOCATION + "\\" + fileName + ".pdf"));
				document.open();
				setRectanglePdf(document);

				Paragraph paragraph = new Paragraph("Cafe Management System", getFont("Header"));
				paragraph.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph);

				Paragraph paragraph2 = new Paragraph(data + "\n \n", getFont("Data"));
				document.add(paragraph2);

				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);

				JsonArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetail"));

				for (int i = 0; i < jsonArray.size(); i++) {
					addRows(table, CafeUtils.getMapFromJson(jsonArray.get(i).toString()));
				}

				document.add(table);

				Paragraph footer = new Paragraph(
						"Total: " + requestMap.get("total") + "\n" + "Thank you for visiting. Please Visit Again!!",
						getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<String>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

			}
			return CafeUtils.getResponseEntity("Required Data Not Found", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRows(PdfPTable table, Map<String, Object> data) {
		String name = String.valueOf(data.get("name"));
		String category = String.valueOf(data.get("category"));
		String quantity = String.valueOf(data.get("quantity"));
		String price = String.valueOf(data.get("price"));

		// Optional: compute total if not provided in data
		double total = Double.parseDouble(price) * Double.parseDouble(quantity);

		table.addCell(name);
		table.addCell(category);
		table.addCell(quantity);
		table.addCell(price);
		table.addCell(String.valueOf(total));
	}

	private void addTableHeader(PdfPTable table) {
		Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});

	}

	private Font getFont(String type) {
		switch (type) {
		case "Header": {
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		}
		case "Data":
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
		default:
			return new Font();
		}
	}

	private void setRectanglePdf(Document document) throws DocumentException {
		// TODO Auto-generated method stub
		Rectangle rectangle = new Rectangle(577, 825, 28, 15);
		rectangle.enableBorderSide(1);
		rectangle.enableBorderSide(2);
		rectangle.enableBorderSide(4);
		rectangle.enableBorderSide(8);
		rectangle.setBorderColor(BaseColor.BLACK);
		rectangle.setBorderWidth(1);
		document.add(rectangle);

	}

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt((String) requestMap.get("total")));
			bill.setProductDetail((String) requestMap.get("productDetail"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billDao.save(bill);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		return requestMap.containsKey("name") && requestMap.containsKey("email")
				&& requestMap.containsKey("paymentMethod") && requestMap.containsKey("productDetail")
				&& requestMap.containsKey("total");
	}

	@Override
	public ResponseEntity<List<Bill>> getBill() {
		List<Bill> list = new ArrayList<>();
		if (jwtFilter.isAdmin()) {
			list = billDao.getAllBills();
		} else {
			list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		try {
			byte[] byteArray = new byte[0];
			if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
			}

			String filePath = CafeContants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
			if (CafeUtils.isFileExist(filePath)) {
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray, HttpStatus.OK);
			} else {
				requestMap.put("isGenerate", false);
				generateReport(requestMap);
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getByteArray(String filePath) throws IOException {
		File initialFile = new File(filePath);
		InputStream inputStream = new FileInputStream(initialFile);
		byte[] byteArray = IOUtils.toByteArray(inputStream);
		inputStream.close();
		return byteArray;
	}

	@Override
	public ResponseEntity<String> deleteBill(int id) {
		try {
			Optional optional = billDao.findById(id);
			if (!optional.isEmpty()) {
				billDao.deleteById(id);
				return CafeUtils.getResponseEntity("Bill Deleted Successfully", HttpStatus.OK);
			}
			return CafeUtils.getResponseEntity("Bill Id Doesn't Exists", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
