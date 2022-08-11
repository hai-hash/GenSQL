package com.mycompany.generaldatainsert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author whyno
 */
public class GeneralDataInsert {
    private static final Logger logger = LogManager.getLogger(GeneralDataInsert.class);
    public static void generalDataQueryInsertWithExcell(String nameDatabase, String nameTable) throws FileNotFoundException, IOException, Exception{
        FileInputStream inputSream = new FileInputStream(new File("datademo.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(inputSream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        logger.info("Thực hiện ghi dữ liệu");

        Iterator<Row> rowIterator = sheet.iterator();
        //create query Insert
        StringBuilder commonQuery = new StringBuilder("INSERT INTO ");
        commonQuery.append(nameDatabase);
        commonQuery.append(".");
        commonQuery.append(nameTable);
        commonQuery.append("(");
        //get list colun of table
        if (rowIterator.hasNext()) {
            Row rowColun = rowIterator.next();
            Iterator<Cell> cells = rowColun.iterator();
            while (cells.hasNext()) {
                Cell cellColun = cells.next();
                CellType celltypeColumn = cellColun.getCellType();
                if (celltypeColumn != CellType.STRING) {
                    throw new Exception("Column table must to type is string");
                }
                if (!cells.hasNext()) {
                    commonQuery.append(cellColun.getStringCellValue().trim());
                } else {
                    commonQuery.append(cellColun.getStringCellValue().trim());
                    commonQuery.append(",");
                }
            }
            commonQuery.append(") VALUE ");
        }
        
        //save insert query into file
        
        File fileWrite = new File("data.txt");
            if(!fileWrite.exists()){
                fileWrite.createNewFile();
            }
            
            FileWriter fw = new FileWriter(fileWrite,true);
            BufferedWriter bw = new BufferedWriter(fw);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            StringBuilder valueDataInsert = new StringBuilder("(");

            Iterator<Cell> iteratorCell = row.iterator();

            while (iteratorCell.hasNext()) {
                Cell cell = iteratorCell.next();
                CellType cellType = cell.getCellType();

                switch (cellType) {
                    case _NONE:

                        break;
                    case BOOLEAN:

                        break;
                    case BLANK:
                        if (!iteratorCell.hasNext()) {
                            valueDataInsert.append("null");
                        } else {
                            valueDataInsert.append("null,");
                        }
                        break;
                    case FORMULA:
                        System.out.print(cell.getCellFormula());
                        System.out.print("\t");

                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                        // In ra giá trị từ công thức
                        System.out.print(evaluator.evaluate(cell).getNumberValue());
                        break;
                    case NUMERIC:
                        if (!iteratorCell.hasNext()) {
                            valueDataInsert.append(cell.getNumericCellValue());
                        } else {
                            valueDataInsert.append(cell.getNumericCellValue());
                            valueDataInsert.append(",");
                        }
                        break;
                    case STRING:
                        if (!iteratorCell.hasNext()) {
                            valueDataInsert.append("'");
                            valueDataInsert.append(cell.getStringCellValue().trim());
                            valueDataInsert.append("'");
                        } else {
                            valueDataInsert.append("'");
                            valueDataInsert.append(cell.getStringCellValue().trim());
                            valueDataInsert.append("',");
                        }
                        break;
                    case ERROR:
                        System.out.print("!");
                        System.out.print("\t");
                        break;
                }

            }
            valueDataInsert.append(")");
            String insertQuery = commonQuery.toString() + valueDataInsert.toString() + "\n";
            bw.write(insertQuery);
        }
        
        bw.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }
}
