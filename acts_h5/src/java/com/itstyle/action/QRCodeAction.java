package com.itstyle.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.ServletActionContext;

import com.itstyle.util.Constants;
import com.itstyle.util.QRCode;
import com.opensymphony.xwork2.Action;

public class QRCodeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String message;
	private String content;
    private String img;
	/**
	 * 生成二维码
	 * @Author	张志朋
	 * @return  String
	 * @Date	2016年7月9日
	 * 更新日志
	 * 2016年7月9日 张志朋  首次创建
	 *
	 */
	public String encoderQRCode(){
		String realpath = ServletActionContext.getServletContext().getRealPath("/file");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String imgName = sdf.format(new Date()) + ".png";
		String  imgPath= realpath+"/"+imgName;
		QRCode.encoderQRCode(content, imgPath, "png");
		message = imgName;
		return Action.SUCCESS;
	}
	/**
	 * 解析二维码
	 * @Author	张志朋
	 * @return  String
	 * @Date	2016年7月9日
	 * 更新日志
	 * 2016年7月9日 张志朋  首次创建
	 *
	 */
	public String decoderQRCode(){
		try {
			String realpath = ServletActionContext.getServletContext().getRealPath("/file");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String imgName = sdf.format(new Date()) + ".png";
			String filePath = realpath+Constants.SF_FILE_SEPARATOR+imgName;
			OutputStream out = new FileOutputStream(filePath);
			QRCode.GenerateImage(img,out);//生成图片
			message   = QRCode.decoderQRCode(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
