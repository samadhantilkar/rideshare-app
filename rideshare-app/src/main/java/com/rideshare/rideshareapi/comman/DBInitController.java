package com.rideshare.rideshareapi.comman;

import com.rideshare.rideshareapi.account.Account;
import com.rideshare.rideshareapi.account.AccountRepository;
import com.rideshare.rideshareapi.car.*;
import com.rideshare.rideshareapi.comman.model.Gender;
import com.rideshare.rideshareapi.driver.Driver;
import com.rideshare.rideshareapi.driver.DriverRepository;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.passenger.PassengerRepository;
import com.rideshare.rideshareapi.role.Role;
import com.rideshare.rideshareapi.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/db-init")
@RequiredArgsConstructor
public class DBInitController {

    private final PasswordEncoder passwordEncoder;
    private final PassengerRepository passengerRepository;
    private final AccountRepository accountRepository;
    private final DriverRepository driverRepository;
    private final ColorRepository colorRepository;
    private final RoleRepository roleRepository;
    private final CarRepository carRepository;

    @GetMapping
    public ResponseEntity<String> initDB(){
        passengerRepository.deleteAll();
        driverRepository.deleteAll();
        accountRepository.deleteAll();
        carRepository.deleteAll();
        colorRepository.deleteAll();
        roleRepository.deleteAll();

        Color white= Color.builder().name("white").build();
        Color black= Color.builder().name("black").build();
        Color yellow= Color.builder().name("yellow").build();
        colorRepository.saveAll(List.of(white,black,yellow));

        Role driver=Role.builder().name("ROLE_DRIVER").description("Driver").build();
        Role passenger= Role.builder().name("ROLE_PASSENGER").description("Passenger").build();
        Role admin=Role.builder().name("ROLE_ADMIN").description("Admin").build();
        roleRepository.saveAll(List.of(driver,passenger,admin));

        Driver luffy= Driver.builder()
                .name("Monkey D. Luffy")
                .account(Account.builder()
                        .username("0000")
                        .password(passwordEncoder.encode("luffy"))
                        .role(driver)
                        .build())
                .car(Car.builder()
                        .brandAndModel("Large Ship")
                        .color(yellow)
                        .carType(CarType.XL)
                        .plateNumber("Thousand Sunny")
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0000")
                .build();

        Driver zoro= Driver.builder()
                .name("Roronoa Zoro")
                .account(Account.builder()
                        .username("0001")
                        .password(passwordEncoder.encode("zoro"))
                        .role(driver)
                        .build())
                .car(Car.builder()
                        .brandAndModel("Caravel")
                        .color(white)
                        .carType(CarType.SEDAN)
                        .plateNumber("Going Merry")
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0001")
                .build();
        driverRepository.saveAll(List.of(zoro,luffy));

        Passenger usopp=Passenger.builder()
                .account(Account.builder()
                        .username("0002")
                        .password(passwordEncoder.encode("usopp"))
                        .role(passenger)
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0002")
                .build();

        Passenger nami= Passenger.builder()
                .account(Account.builder()
                        .username("0004")
                        .password(passwordEncoder.encode("nami"))
                        .role(passenger)
                        .build())
                .gender(Gender.FEMALE)
                .phoneNumber("0004")
                .build();

        passengerRepository.saveAll(List.of(usopp,nami));

        Account sanji= Account.builder()
                .username("0003")
                .role(admin)
                .password(passwordEncoder.encode("sanji"))
                .build();

        accountRepository.save(sanji);

        return ResponseEntity.ok("success");
    }
}
