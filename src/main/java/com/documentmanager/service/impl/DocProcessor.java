package com.documentmanager.service.impl;

import com.documentmanager.config.Constants;
import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocColNameStoreRepository;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.dto.DocStoreDTO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocProcessor implements Runnable {

    DocStore docStore;

    DocStoreDTO docStoreDTO;

    public DocStoreDTO getDocStoreDTO() {
        return docStoreDTO;
    }

    public void setDocStoreDTO(DocStoreDTO docStoreDTO) {
        this.docStoreDTO = docStoreDTO;
    }

    @Autowired
    DocColNameStoreRepository docColNameStoreRepository;

    @Autowired
    DocColValueStoreRepository docColValueStoreRepository;

    @Autowired
    DocStoreRepository docStoreRepository;

    @Override
    public void run() {
        try {
            docStore = docStoreRepository.findById(docStoreDTO.getId()).get();
            docStoreRepository.updateProcessStatus(docStore.getId(), Constants.FILE_UNDER_PROCESS);
            System.out.println(docStore.getId());
            long start = System.currentTimeMillis();
            InputStream stream = new ByteArrayInputStream(docStoreDTO.getFileObject());
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            int rowCount = 0;
            int colCount = 0;
            List<DocColNameStore> docColNameStoreList = new LinkedList<>();
            List<DocColValueStore> docColValueStoreList = new LinkedList<>();
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    DataFormatter formatter = new DataFormatter();
                    String strValue = formatter.formatCellValue(nextCell);
                    System.out.println(strValue);
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
                        docColValueStoreList.add(docColValueStore);
                    }
                }
                if (rowCount == 0) {
                    docStore.setDocColNameStores(docColNameStoreList);
                } else {
                    docStore.setDocColValueStores(docColValueStoreList);
                }
                rowCount++;
            }
            docStoreRepository.save(docStore);
            docStoreRepository.updateProcessStatus(docStore.getId(), Constants.FILE_PROCESSED);
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
