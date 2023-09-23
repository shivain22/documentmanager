package com.documentmanager.service.impl;

import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocColNameStoreRepository;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.DocColNameStoreService;
import com.documentmanager.service.DocColValueStoreService;
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
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocProcessor implements Runnable {

    DocStore docStore;


    DocStoreDTO docStoreDTO;

    public DocStore getDocStore() {
        return docStore;
    }

    public void setDocStore(DocStore docStore) {
        this.docStore = docStore;
    }
    @Autowired
    DocColNameStoreRepository docColNameStoreRepository;
    @Autowired
    DocColValueStoreRepository docColValueStoreRepository;
    @Autowired
    DocStoreRepository docStoreRepository;
    @Autowired
    DocColNameStoreService docColNameStoreService;

    @Autowired
    DocColValueStoreService docValueStoreService;

    @Override
    public void run() {
        try {
            docStore = docStoreRepository.getReferenceById(docStoreDTO.getId());
            long start = System.currentTimeMillis();
            InputStream stream = new ByteArrayInputStream(docStore.getFileObject());
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            int rowCount = 0;
            int colCount = 0;
            List<DocColNameStore> docColNameStoreList = new LinkedList<>();
            List<DocColValueStore> docValueStoreList = new LinkedList<>();
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
                        docColNameStore.setDocStore(docStore);
                        docColNameStore.setColName(strValue);
                        docColNameStoreList.add(docColNameStore);
                    } else if (rowCount > 0) {
                        DocColValueStore docColValueStore = new DocColValueStore();
                        docColValueStore.setDocStore(docStore);
                        docColValueStore.setDocColNameStore(docColNameStoreList.get(columnIndex));
                        docColValueStore.setColValue(strValue);
                        docValueStoreList.add(docColValueStore);
                    }
                }
                if (rowCount == 0) {
                    docColNameStoreRepository.saveAll(docColNameStoreList);
                } else {
                    docColValueStoreRepository.saveAll(docValueStoreList);
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
        docStore.getFileName();
    }
}
