package br.vianna.aula.jsf;

import br.vianna.aula.jsf.model.domain.Cliente;
import br.vianna.aula.jsf.model.domain.Log;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class ProjectJsfSpringApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProjectJsfSpringApplication.class, args);
    }
    
    @Autowired
    EntityManager em;

    
    @Transactional
    @Override    
    public void run(String... args) throws Exception {
        
        Cliente c = new Cliente(0, "Thiago", "Otoni", "t@otoni", "00099900099", "36052560", "Rua Maria", "Ladeira", "Juiz de Fora", "Brasil", "Mg", "5532991167607", new ArrayList<Log>());
        em.persist(c);
    }

}
