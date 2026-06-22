package com.foro.backend.config;

import com.foro.backend.models.AdminUser;
import com.foro.backend.models.Foro;
import com.foro.backend.models.StudentUser;
import com.foro.backend.models.User;
import com.foro.backend.repositories.ForoRepository;
import com.foro.backend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

// Seed de datos iniciales en la BD relacional (MySQL).
//
// Se ejecuta automáticamente al levantar el proyecto gracias a la interfaz
// CommandLineRunner: Spring busca todos los beans que la implementen y los
// corre después de levantar el contexto.
//
// Inserta 5 usuarios (1 admin + 4 students) y 6 foros que coinciden con los
// del seed del BFF (mismas facultades y la misma password "password123" para
// que se pueda probar el login de extremo a extremo).
//
// Si las tablas ya tienen datos, NO inserta nada — evita duplicar foros y
// usuarios si el seed se ejecuta varias veces (porque cada `mvn spring-boot:run`
// arranca todo de nuevo y vuelve a llamar al CommandLineRunner).
@Configuration
public class DataSeeder implements CommandLineRunner {

    // Inyección por constructor (mejor práctica que @Autowired en campos:
    // permite que los campos sean final y facilita testear).
    private final UserRepository userRepository;
    private final ForoRepository foroRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ForoRepository foroRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.foroRepository = foroRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedForos();
    }

    // ── Usuarios ────────────────────────────────────────────────────────────
    private void seedUsers() {
        if (userRepository.count() > 0) {
            System.out.println("DataSeeder: ya hay usuarios en la BD, skip");
            return;
        }

        // Hasheamos la password una sola vez con BCrypt (one-way + salt automático).
        // El bean PasswordEncoder lo define SecurityConfig (GIA-27).
        final String passwordHash = passwordEncoder.encode("password123");

        // 1 admin
        // El campo `role` usa la convención funcional del equipo: "admin" y
        // "user" (en minúscula). NO se confunde con el discriminator
        // `user_type` de JPA — eso sigue siendo "ADMIN" / "STUDENT" porque es
        // un detalle interno de la jerarquía OO (AdminUser / StudentUser).
        AdminUser admin = new AdminUser();
        admin.setEmail("admin@uap.edu.ar");
        admin.setUsername("admin");
        admin.setRole("admin");
        admin.setPasswordHash(passwordHash);

        // 4 students con role "user". Internamente JPA los persiste con
        // user_type = "STUDENT" (eso lo hace el discriminator automáticamente),
        // pero el rol funcional expuesto al frontend / BFF es "user".
        StudentUser gianna = buildStudent("gianna@uap.edu.ar", "gianna", passwordHash);
        StudentUser malena = buildStudent("malena@uap.edu.ar", "malena", passwordHash);
        StudentUser milena = buildStudent("milena@uap.edu.ar", "milena", passwordHash);
        StudentUser jperez = buildStudent("jperez@uap.edu.ar", "jperez", passwordHash);

        List<User> nuevos = List.of(admin, gianna, malena, milena, jperez);
        userRepository.saveAll(nuevos);
        System.out.println("DataSeeder: " + nuevos.size() + " usuarios insertados");
    }

    private StudentUser buildStudent(String email, String username, String passwordHash) {
        StudentUser u = new StudentUser();
        u.setEmail(email);
        u.setUsername(username);
        u.setRole("user");
        u.setPasswordHash(passwordHash);
        return u;
    }

    // ── Foros ───────────────────────────────────────────────────────────────
    // Los nombres y las claves de facultad matchean con los del seed del BFF
    // (src/seed/seed.js) y con FACULTY_CONFIG del frontend. Así, una vez
    // ejecutado el sync, los foros del Backend y del BFF coinciden 1:1.
    private void seedForos() {
        if (foroRepository.count() > 0) {
            System.out.println("DataSeeder: ya hay foros en la BD, skip");
            return;
        }

        List<Foro> foros = List.of(
            buildForo("Humanidades",
                      "Carreras de letras, historia, filosofía y educación.",
                      "humanidades"),
            buildForo("Ciencias Económicas",
                      "Administración, contabilidad, comercio y economía.",
                      "economicas"),
            buildForo("Teología",
                      "Estudios bíblicos, teológicos y pastorales.",
                      "teologia"),
            buildForo("Ciencias de la Salud",
                      "Medicina, enfermería, nutrición y bioquímica.",
                      "salud"),
            buildForo("Instituto Superior",
                      "Carreras técnicas y tecnicaturas del instituto.",
                      "instituto"),
            buildForo("Comunidad UAP",
                      "Espacio general para toda la universidad.",
                      "general")
        );

        foroRepository.saveAll(foros);
        System.out.println("DataSeeder: " + foros.size() + " foros insertados");
    }

    private Foro buildForo(String nombre, String descripcion, String facultad) {
        Foro f = new Foro();
        f.setNombre(nombre);
        f.setDescripcion(descripcion);
        f.setFacultad(facultad);
        return f;
    }
}
