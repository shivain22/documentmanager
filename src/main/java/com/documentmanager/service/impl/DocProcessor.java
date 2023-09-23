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
import java.util.Iterator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            InputStream stream = new ByteArrayInputStream(docStoreDTO.getFileObject());
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            int count = 0;
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    DataFormatter formatter = new DataFormatter();
                    String strValue = formatter.formatCellValue(nextCell);
                }
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
