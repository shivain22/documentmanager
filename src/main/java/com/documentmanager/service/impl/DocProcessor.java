package com.documentmanager.service.impl;

import com.documentmanager.domain.DocStore;
import com.documentmanager.service.dto.DocStoreDTO;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocProcessor implements Runnable {

    DocStoreDTO docStoreDTO;

    public DocStoreDTO getDocStoreDTO() {
        return docStoreDTO;
    }

    public void setDocStoreDTO(DocStoreDTO docStoreDTO) {
        this.docStoreDTO = docStoreDTO;
    }

    @Autowired
    DocColNameStoreService docColNameStoreService;

    @Autowired
    DocValueStoreService docValueStoreService;

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            InputStream stream = new ByteArrayInputStream(docStoreDTO.getFileObject());
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            int rowCount = 0;
            int colCount = 0;
            List<DocColNameStore> docColNameStoreList = new LinkedList<>();
            List<DocValueStore> docValueStoreList = new LinkedList<>();
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    DataFormatter formatter = new DataFormatter();
                    String strValue = formatter.formatCellValue(nextCell);
                    if (rowCount == 0) {
                        DocColNameStore docColNameStore = new DocColNameStore();
                        docColNameStore.setDocStore(docStoreDTO);
                        docColNameStore.setColName(strValue);
                        colNameStoreList.add(docColNameStore);
                    } else if (rowCount > 0) {
                        DocValueStore docValueStore = new DocValueStore();
                        docValueStore.setDocStore(docStoreDTO);
                        docValueStore.setDocColStore(docColNameStoreList.get(columnIndex).getId());
                        docValueStore.setValue(strValue);
                        docValueStoreList.add(docValueStore);
                    }
                }
                if (rowCount == 0) {
                    docColNameStoreService.saveAll(docColNameStoreList);
                } else {
                    docValueStoreService.saveAll(docValueStoreList);
                }
                rowCount++;
            }
            workbook.close();
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        }
        docStoreDTO.getFileName();
    }
}
