package com.armtimes.armtimes.service;


import com.armtimes.armtimes.entity.User;
import com.armtimes.armtimes.repository.UserRepository;
import com.armtimes.armtimes.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) // there is also PROTOTYPE scope for bean. If we use prototype,
                                                        // every time Spring makes a new object. Prototype is a regular bean.
                                                        // Singleton is a default value
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final DateUtil dateUtil; // this is a component, it's a bean
    private final MailService mailService;
    @Value("${task.management.images.folder}")
    private String folderPath;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void saveUser(User user, MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            user.setPicUrl(fileName);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        mailService.sendEmail(user.getEmail(), "Welcome", "Hi " + user.getName() + " " + user.getSurname() +
         "\n" + "You have successfully registered!!!");
    }

    public byte[] getUserService(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

}
