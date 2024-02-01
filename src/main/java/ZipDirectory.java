import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDirectory implements Archivos {

    private String ruta;

    public ZipDirectory(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean comprimir() {
        try {
            FileOutputStream fos = new FileOutputStream(ruta + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(ruta);

            zipDirectory(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void zipDirectory(File folder, String baseName, ZipOutputStream zipOut) throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, baseName + "/" + file.getName(), zipOut);
                } else {
                    zipFile(file, baseName, zipOut);
                }
            }
        }
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName + "/" + fileToZip.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        fis.close();
    }
}