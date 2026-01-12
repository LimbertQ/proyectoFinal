/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thecelestials.view.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author pc
 */
public class Loader {
    public static BufferedImage ImageLoader(String path) {
        
        // 1. Intentar obtener el recurso primero
        URL resourceUrl = Loader.class.getResource(path);

        if (resourceUrl == null) {
            // 2. Si el recurso es nulo, reportamos que no se encontró
            System.err.println("ERROR DE RECURSO: No se pudo encontrar la imagen en la ruta: " + path);
            return null;
        }

        try {
            // 3. Intentar leer la imagen usando la URL (que ya sabemos que no es null)
            return ImageIO.read(resourceUrl);
            
        } catch (IOException e) {
            // 4. Captura si la lectura falla (el archivo no es una imagen válida)
            System.err.println("ERROR DE LECTURA: Fallo al decodificar la imagen en la ruta: " + path);
            e.printStackTrace();
            
        } catch (IllegalArgumentException e) {
            // 5. Captura si ImageIO.read() recibe un argumento no válido (aunque la validamos arriba)
            //    Esto es bueno tenerlo por seguridad.
            System.err.println("ERROR ARGUMENTO: El argumento pasado a ImageIO.read() no fue válido.");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Clip loadSound(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Loader.class.getResource(path)));
            return clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String loadMedia(String path) {
        // La forma correcta de referenciar la clase
        URL resource = Loader.class.getResource(path);
        
        if (resource == null) {
            System.err.println("Error: No se encontró el recurso de media en la ruta: " + path);
            return null;
        }
        return resource.toExternalForm();
    }
    
    public static Font loadFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Loader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
