package com.tarbus.services;

import com.tarbus.models.Company;
import com.tarbus.models.User;

import java.util.List;

public interface CompanyService {
  List<Company> getMyCompanies(User user );
  List<Company> getUserCompanies(String userId );
  List<Company> getAll();
  Company getCompanyById(Long id );
}
