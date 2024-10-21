package fr.mkadia.mkadiaapi.services;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @Test
    void should_file_exist(){
        String url = "D:\\Projets-Prof\\mkadia\\mkadia-app-files\\SelectionDataColum.png";
        Path path = Paths.get(url);
        assertTrue(Files.exists(path));
    }
}