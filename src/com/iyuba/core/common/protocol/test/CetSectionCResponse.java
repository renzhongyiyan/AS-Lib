package com.iyuba.core.common.protocol.test;

import java.util.ArrayList;
import java.util.Vector;

import com.iyuba.core.common.network.xml.Utility;
import com.iyuba.core.common.network.xml.kXMLElement;
import com.iyuba.core.common.protocol.BaseXMLResponse;
import com.iyuba.core.common.sqlite.mode.test.CetFillInBlank;
import com.iyuba.core.common.sqlite.mode.test.CetText;

public class CetSectionCResponse extends BaseXMLResponse {
	public ArrayList<CetFillInBlank> answerList = new ArrayList<CetFillInBlank>();
	public ArrayList<CetText> textList = new ArrayList<CetText>();
	public int type;

	public CetSectionCResponse(int type) {
		this.type = type;
	}

	@Override
	protected boolean extractBody(kXMLElement headerEleemnt,
			kXMLElement bodyElement) {
		// TODO Auto-generated method stub
		Vector rankVector = bodyElement.getChildren();
		int size = rankVector.size();
		kXMLElement ranKXMLElement;
		Vector testVector;
		CetFillInBlank cetFillInBlank;
		kXMLElement rowKXMLElement;
		int length;
		CetText cetText;
		for (int i = 0; i < size; i++) {
			ranKXMLElement = (kXMLElement) rankVector.elementAt(i);
			if (ranKXMLElement.getTagName().equals("testData")) {
				testVector = ranKXMLElement.getChildren();
				length = testVector.size();
				for (int j = 0; j < length; j++) {
					rowKXMLElement = (kXMLElement) testVector.elementAt(j);
					if (rowKXMLElement.getTagName().equals("row")) {
						cetFillInBlank = new CetFillInBlank();
						cetFillInBlank.id = Utility.getSubTagContent(
								rowKXMLElement, "number");
						cetFillInBlank.question = Utility.getSubTagContent(
								rowKXMLElement, "question");
						cetFillInBlank.answer = Utility.getSubTagContent(
								rowKXMLElement, "answer");
						cetFillInBlank.sound = Utility.getSubTagContent(
								rowKXMLElement, "sound");
						String year = Utility.getSubTagContent(rowKXMLElement,
								"testTime");
						String url = "http://cetsoundsvip.iyuba.com/" + type
								+ "/" + year + "/";
						cetFillInBlank.sound = url + cetFillInBlank.sound;
						cetFillInBlank.yourAnswer = "";
						cetFillInBlank.allSound = url;
						answerList.add(cetFillInBlank);
					}
				}
			}
			if (ranKXMLElement.getTagName().equals("textData")) {
				testVector = ranKXMLElement.getChildren();
				length = testVector.size();
				for (int j = 0; j < length; j++) {
					rowKXMLElement = (kXMLElement) testVector.elementAt(j);
					if (rowKXMLElement.getTagName().equals("row")) {
						cetText = new CetText();
						cetText.id = Utility.getSubTagContent(rowKXMLElement,
								"number");
						cetText.index = Utility.getSubTagContent(
								rowKXMLElement, "indexNumber");
						cetText.time = Utility.getSubTagContent(rowKXMLElement,
								"timing1");
						cetText.sentence = Utility.getSubTagContent(
								rowKXMLElement, "sentence");
						cetText.qwords = Utility.getSubTagContent(
								rowKXMLElement, "qwords");
						textList.add(cetText);
					}
				}
			}
		}
		return true;
	}

}
