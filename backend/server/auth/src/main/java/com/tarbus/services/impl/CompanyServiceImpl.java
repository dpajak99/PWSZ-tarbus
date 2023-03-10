package com.tarbus.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.models.Company;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.CompanyRepository;
import com.tarbus.services.CompanyService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hibernate.internal.util.collections.ArrayHelper.toList;

@Service
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository companyRepository;

  @Autowired
  public CompanyServiceImpl(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  @Override
  public List<Company> getMyCompanies(User user ) {
    return companyRepository.getUserCompanies(user.getId());
  }

  @Override
  public List<Company> getUserCompanies(String userId) {
    return companyRepository.getUserCompanies(userId);
  }

  @Override
  public List<Company> getAll() {
    return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
      .collect(Collectors.toList());
  }

  @Override
  public Company getCompanyById(Long id) {
    return companyRepository.findById(id).get();
  }
}
