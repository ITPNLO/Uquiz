package com.itfrees.ames.utils;

import com.itfrees.ames.bean.VocaInfo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VocaExcelReader {
    /**
     * ファイルからデータを取得する。
     * @param is - inputstream of file
     * @return Object[0] : vocaList, Object[1] : vocaGroup
     */
    public Object[] getVocaList(InputStream is) throws Exception {
        Object[] results = new Object[2];
        List<VocaInfo> vocaList = new ArrayList<>();
        String[] vocaGroup = new String[2];
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(is);
            HSSFSheet curSheet;
            HSSFRow curRow;
            HSSFCell curCell;
            String title = "";
            String language = "";
            VocaInfo vocaInfo;
            int nullCnt = 0;
            ImportFormat importFormat = ImportFormat.DATA_FORMAT;
            curSheet = workbook.getSheetAt(0);
            for (int rowIndex = 0; rowIndex <= curSheet.getPhysicalNumberOfRows(); rowIndex++) {
                curRow = curSheet.getRow(rowIndex);
                // タイトル取得
                if (rowIndex == 0 && null != curRow) {
                    if (Consts.TITLE.equals(curRow.getCell(0).getStringCellValue())) {
                        title = curRow.getCell(1).getStringCellValue();
                        vocaGroup[0] = title;
                    }
                }
                // 言語種類取得
                if (rowIndex == 1 && null != curRow) {
                    if (Consts.LANGUAGE.equals(curRow.getCell(0).getStringCellValue())) {
                        language = curRow.getCell(1).getStringCellValue();
                        vocaGroup[1] = language;
                    }
                }
                //タイトルと言語種類があればインデックスを４で設定（AMESフォーマット）
                if ((!title.isEmpty() && !language.isEmpty()) &&
                        importFormat != ImportFormat.AMES_FORMAT) {
                    importFormat = ImportFormat.AMES_FORMAT;
                    vocaList.clear();
                    rowIndex = 4;
                    curRow = curSheet.getRow(rowIndex);
                }
                vocaInfo = new VocaInfo();
                if (null != curRow) {
                    for (int cellIndex = 0; cellIndex < curRow.getLastCellNum(); cellIndex++) {
                        curCell = curRow.getCell(cellIndex);
                        String value = "";
                        if (null != curCell) {
                            value = curCell.getStringCellValue();
                        }
                        switch (cellIndex) {
                            case 0:
                                vocaInfo.setVoca1(value);
                                break;
                            case 1:
                                vocaInfo.setVoca2(value);
                                break;
                            case 2:
                                vocaInfo.setVoca3(value);
                                break;
                            case 3:
                                vocaInfo.setVoca4(value);
                                break;
                            case 5:
                                vocaInfo.setVoca5(value);
                                break;
                            case 6:
                                vocaInfo.setVoca6(value);
                                break;
                            case 7:
                                vocaInfo.setVoca7(value);
                                break;
                            case 8:
                                vocaInfo.setVoca8(value);
                                break;
                            case 9:
                                vocaInfo.setVoca9(value);
                                break;
                            case 10:
                                vocaInfo.setVoca10(value);
                                break;
                            default:
                                break;

                        }// switch
                    } // for
                    if ("".equals(vocaInfo.getVoca1()) && "".equals(vocaInfo.getVoca2()) &&
                            "".equals(vocaInfo.getVoca3()) && "".equals(vocaInfo.getVoca4()) &&
                            "".equals(vocaInfo.getVoca5()) && "".equals(vocaInfo.getVoca6()) &&
                            "".equals(vocaInfo.getVoca7()) && "".equals(vocaInfo.getVoca8()) &&
                            "".equals(vocaInfo.getVoca9()) && "".equals(vocaInfo.getVoca10())) {
                        nullCnt++;
                    } else {
                        nullCnt = 0;
                        vocaList.add(vocaInfo);
                    }
                } else {
                    nullCnt++;
                }
                if (10 == nullCnt) {
                    results[0] = vocaList;
                    results[1] = vocaGroup;
                    return results;
                }
            }
        } finally {
            try {
                if (workbook != null)
                    workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        results[0] = vocaList;
        results[1] = vocaGroup;
        return results;
    }

    public enum ImportFormat {
        AMES_FORMAT, // AMES Format
        DATA_FORMAT, // Dataのみあるフォーマット, 他のラベル（タイトル）フォーマット
    }
}
