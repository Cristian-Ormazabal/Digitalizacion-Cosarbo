package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
   //metodo para buscar por correo
    Optional<Usuario> findByEmail(String email);
}