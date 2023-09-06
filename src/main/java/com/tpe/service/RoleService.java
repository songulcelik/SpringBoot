package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.enums.UserRole;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class RoleService {

    private  final RoleRepository roleRepository;

    public Role getRoleByType(UserRole roleType){
        return roleRepository.findByName(roleType).orElseThrow(()->
                new ResourceNotFoundException("Role not found : " + roleType.name()));
    }
}