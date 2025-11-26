package com.hidroterapia_ondas.config;

import com.hidroterapia_ondas.model.Service;
import com.hidroterapia_ondas.repository.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ServiceRepository repo;

    public DataLoader(ServiceRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {

        if (repo.count() == 0) {
            Service s1 = new Service();
            s1.setTitulo("Sesión Hidroterapia");
            s1.setPrecio(120000);
            s1.setDescripcion("Disfruta de una sesión 100% personalizada en agua caliente, guiada únicamente por una fisioterapeuta especializada. Durante una hora completa recibirás atención individual adaptada a tus necesidades: alivio del dolor, mejora de la movilidad, relajación profunda y recuperación funcional. El agua caliente potencia los efectos de la terapia, favoreciendo la circulación, disminuyendo la tensión muscular y creando un ambiente seguro y cómodo para trabajar cuerpo y mente. Una experiencia única de bienestar y salud en un espacio diseñado solo para ti.");
            s1.setImagenUrl("assets/images/HIDRO.jpg");
            repo.save(s1);

            Service s2 = new Service();
            s2.setTitulo("Fisioterapia");
            s2.setPrecio(90000);
            s2.setDescripcion("Atención totalmente personalizada, realizada por una fisioterapeuta profesional que te acompaña durante toda la sesión. En una hora completa trabajamos de forma individual tus necesidades específicas: valoración del movimiento, alivio del dolor, técnicas manuales, ejercicios terapéuticos y orientación para tu recuperación. El objetivo es mejorar tu movilidad, acelerar procesos de rehabilitación y brindarte herramientas para que te sientas mejor en tu día a día. Una experiencia de cuidado integral en un ambiente profesional, seguro y dedicado solo a ti.");
            s2.setImagenUrl("assets/images/FISIO.jpg");
            repo.save(s2);

            Service s3 = new Service();
            s3.setTitulo("Masaje Relajante");
            s3.setPrecio(90000);
            s3.setDescripcion("Un espacio pensado para ti, donde podrás desconectar del estrés y recuperar tu energía. Durante una hora disfrutarás de un masaje personalizado en un ambiente cálido y tranquilo, con maniobras suaves que ayudan a liberar tensiones, relajar los músculos y aportar una sensación profunda de calma. Un momento de autocuidado para reconectar contigo y salir renovado/a.");
            s3.setImagenUrl("assets/images/MASAJE.jpg");
            repo.save(s3);

            Service s4 = new Service();
            s4.setTitulo("Descarga Muscular");
            s4.setPrecio(90000);
            s4.setDescripcion("Sesión personalizada, realizada por una fisioterapeuta, enfocada en aliviar la sobrecarga muscular causada por entrenamiento, esfuerzo físico o tensión acumulada. Durante una hora se aplican técnicas manuales profundas que ayudan a reducir la rigidez, mejorar la circulación y acelerar la recuperación. Es la opción ideal para deportistas o personas activas que buscan optimizar su rendimiento, prevenir lesiones y mantener sus músculos en equilibrio.");
            s4.setImagenUrl("assets/images/DESCARGA.jpg");
            repo.save(s4);

            Service s5 = new Service();
            s5.setTitulo("Medicina Osteopática");
            s5.setPrecio(90000);
            s5.setDescripcion("Atención especializada realizada por la Dra. Miriam Arenas, médica osteópata. En cada sesión se emplean técnicas manuales que buscan restaurar el equilibrio del cuerpo, mejorar la movilidad y aliviar dolores o tensiones de manera integral. La consulta es 100% personalizada, con un enfoque global que considera la relación entre músculos, articulaciones y sistemas del cuerpo para favorecer la salud y el bienestar.");
            s5.setImagenUrl("assets/images/OSTEOPATIA.jpg");
            repo.save(s5);
        }
    }
}