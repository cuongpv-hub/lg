package vn.vsd.agro.converter;

import java.util.List;

import org.bson.types.ObjectId;
import org.jsoup.Jsoup;

import vn.vsd.agro.context.IUser;
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.BaseItem;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.Company;
import vn.vsd.agro.domain.Country;
import vn.vsd.agro.domain.District;
import vn.vsd.agro.domain.Farmer;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.IdNameEmbed;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.News;
import vn.vsd.agro.domain.PO;
import vn.vsd.agro.domain.POEmbed;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.Province;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;
import vn.vsd.agro.domain.embed.ScientistMajorEmbed;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.domain.embed.UserEmbed;
import vn.vsd.agro.dto.ArticleCreateDto;
import vn.vsd.agro.dto.ArticleDto;
import vn.vsd.agro.dto.ArticleSimpleDto;
import vn.vsd.agro.dto.BaseItemCreateDto;
import vn.vsd.agro.dto.BaseResourceCreateDto;
import vn.vsd.agro.dto.CommonCategoryDto;
import vn.vsd.agro.dto.ContactDto;
import vn.vsd.agro.dto.CountryDto;
import vn.vsd.agro.dto.DTO;
import vn.vsd.agro.dto.DTOEmbed;
import vn.vsd.agro.dto.DistrictDto;
import vn.vsd.agro.dto.IdCodeNameDto;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.ImageItemDto;
import vn.vsd.agro.dto.LandCreateDto;
import vn.vsd.agro.dto.LandDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.LinkLandDto;
import vn.vsd.agro.dto.LinkProductDto;
import vn.vsd.agro.dto.LinkProjectDto;
import vn.vsd.agro.dto.LinkScientistDto;
import vn.vsd.agro.dto.LocationAddressDto;
import vn.vsd.agro.dto.LocationDto;
import vn.vsd.agro.dto.NewsCreateDto;
import vn.vsd.agro.dto.NewsDto;
import vn.vsd.agro.dto.NewsSimpleDto;
import vn.vsd.agro.dto.ProductCreateDto;
import vn.vsd.agro.dto.ProductDto;
import vn.vsd.agro.dto.ProductSimpleDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ProjectCreateDto;
import vn.vsd.agro.dto.ProjectDto;
import vn.vsd.agro.dto.ProjectSimpleDto;
import vn.vsd.agro.dto.ProvinceDto;
import vn.vsd.agro.dto.UserDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.dto.embed.UserEmbedDto;
import vn.vsd.agro.util.AddressUtils;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.StringUtils;

public class DomainConverter {
	/****************************
	 * Common Category 
	 ****************************/
	public static CommonCategoryDto createCommonCategoryDto(CommonCategory domain, String typeName) {
		if (domain == null) {
			return null;
		}
		
		CommonCategoryDto dto = new CommonCategoryDto();
		copyPO(domain, dto);
		
		dto.setType(domain.getType());
		dto.setTypeName(typeName);
		dto.setCode(domain.getCode());
		dto.setName(domain.getName());
		dto.setIndex(domain.getIndex());
		dto.setMain(domain.isMain());
		
		return dto;
	}
	
	/***************************
     * Client, Organization, Role & User
     ***************************/
	public static UserDto createUserDto(User domain) {
        if (domain == null) {
        	return null;
        }
        
        UserDto dto = new UserDto();
        copyPO(domain, dto);
        
        dto.setName(domain.getName());
        dto.setEmail(domain.getEmail());
        dto.setMobilePhone(domain.getMobilePhone());
        dto.setCompanyPhone(domain.getCompanyPhone());
        dto.setHomePhone(domain.getHomePhone());
        dto.setRoles(domain.getRoles());
        dto.setAddress(domain.getAddress());
        
        // TODO: 
    	if (domain.getAccessOrgs() != null) {
            for (ObjectId value : domain.getAccessOrgs()) {
                if (value != null) {
                    dto.addAccessOrg(value.toString());
                }
            }
        }            

        return dto;
    }
	
	public static UserEmbedDto createUserEmbedDto(User domain) {
		if (domain == null) {
			return null;
		}
		
		UserEmbedDto dto = new UserEmbedDto();
		copyPO(domain, dto);
		
		dto.setEmail(domain.getEmail());
		dto.setName(domain.getName());
		
		return dto;
	}
	
	public static UserEmbedDto createUserEmbedDto(UserEmbed domain) {
		if (domain == null) {
			return null;
		}
		
		UserEmbedDto dto = new UserEmbedDto();
		copyPO(domain, dto);
		
		dto.setEmail(domain.getEmail());
		dto.setName(domain.getName());
		
		return dto;
	}
	
	public static ContactDto createContactDto(User domain, String alias) {
		if (domain == null) {
			return null;
		}
		
		ContactDto dto = new ContactDto();
		copyContactDto(domain, dto, alias);
		
		return dto;
	}
	
	public static ProfileDto createProfileDto(User user, Company company, 
			Farmer farmer, Scientist scientist, String dateFormat) {
		if (user == null) {
			return null;
		}
		
		ProfileDto dto = new ProfileDto();
		DomainConverter.copyContactDto(user, dto, "");
		
		dto.setEditable(false);
		dto.setCompany(false);
		dto.setFarmer(false);
		dto.setScientist(false);
		
		// Company
		if (user.isCompany()) {
			dto.setCompany(true);
			
			if (company != null) {
				dto.setTaxCode(company.getTaxCode());
				dto.setDescription(company.getDescription());
				
				if (company.getType() != null) {
					dto.setType(DomainConverter.createIdCodeNameDto(company.getType()));
				}
				
				List<IdCodeNameEmbed> companyFields = company.getCompanyFields();
				if (companyFields != null) {
					for (IdCodeNameEmbed companyField : companyFields) {
						if (companyField != null) {
							dto.addCompanyField(DomainConverter.createIdCodeNameDto(companyField));
						}
					}
				}
			}
			
			// TODO: Add linked scientists
		}
		
		// Farmer
		if (user.isFarmer()) {
			dto.setFarmer(true);
			
			if (farmer != null) {
				dto.setGender(farmer.getGender());
				
				if (farmer.getBirthDay() != null) {
					dto.setBirthDay(farmer.getBirthDay().format(dateFormat));
				}
			}
		}
		
		// Scientist
		if (user.isScientist()) {
			dto.setScientist(true);
			
			if (scientist != null) {
				dto.setGender(scientist.getGender());
				
				if (scientist.getBirthDay() != null) {
					dto.setBirthDay(scientist.getBirthDay().format(dateFormat));
				}
				
				List<ScientistMajorEmbed> scientistMajors = scientist.getScientistMajors();
				if (scientistMajors != null) {
					for (ScientistMajorEmbed scientistMajor : scientistMajors) {
						if (scientistMajor != null) {
							dto.addScientistMajor(DomainConverter.createIdCodeNameDto(scientistMajor));
						}
					}
				}
				
				List<IdCodeNameEmbed> literacies = scientist.getLiteracies();
				if (literacies != null) {
					for (IdCodeNameEmbed literacy : literacies) {
						if (literacy != null) {
							dto.addLiteracy(DomainConverter.createIdCodeNameDto(literacy));
						}
					}
				}
				
				dto.setTitle(scientist.getTitle());
				dto.setPosition(scientist.getPosition());
				dto.setWorkplace(scientist.getWorkplace());
			}
		}
		
		return dto;
	}
	
	
	
	
	
	
	public static LinkScientistDto createLinkScientistDto(Link domain, 
			User user, Scientist scientist, User currentUser, String dateFormat) {
    	if (domain == null || user == null) {
    		return null;
    	}
    	
    	LinkScientistDto dto = new LinkScientistDto();
    	copyContactDto(user, dto, "");
    	
    	dto.setOwner(false);
    	dto.setLinkId(domain.idAsString());
    	
    	if (domain.getStatus() != null) {
    		dto.setStatus(createStatusEmbedDto(domain.getStatus()));
    	} else {
    		dto.setStatus(StatusEmbedDto.defaulStatus());
    	}
    	
    	if (scientist != null) {
    		dto.setGender(scientist.getGender());
    		
    		if (scientist.getBirthDay() != null) {
    			dto.setBirthDay(scientist.getBirthDay().format(dateFormat));
    		}
    		
    		List<ScientistMajorEmbed> scientistMajors = scientist.getScientistMajors();
			if (scientistMajors != null) {
				for (ScientistMajorEmbed scientistMajor : scientistMajors) {
					if (scientistMajor != null) {
						dto.addScientistMajor(DomainConverter.createIdCodeNameDto(scientistMajor));
					}
				}
			}
			
			List<IdCodeNameEmbed> literacies = scientist.getLiteracies();
			if (literacies != null) {
				for (IdCodeNameEmbed literacy : literacies) {
					if (literacy != null) {
						dto.addLiteracy(DomainConverter.createIdCodeNameDto(literacy));
					}
				}
			}
			
    		dto.setTitle(scientist.getTitle());
			dto.setPosition(scientist.getPosition());
			dto.setWorkplace(scientist.getWorkplace());
			
			if (currentUser != null && currentUser.getId().equals(scientist.getUserId())) {
	    		dto.setOwner(true);
	    	}
    	}
    	
        return dto;
    }
	
	/****************** Common Category, Currency *******************/
    public static IdCodeNameDto createIdCodeNameDto(IdCodeNameEmbed domain) {
        if (domain == null) {
        	return null;
        }
        
        
        IdCodeNameDto dto = new IdCodeNameDto();

        dto.setId(domain.idAsString());
        dto.setCode(domain.getCode());
        dto.setName(domain.getName());

        return dto;
    }

    public static IdNameDto createIdNameDto(IdNameEmbed domain) {
        if (domain == null) {
        	return null;
        }
        
        IdNameDto dto = new IdNameDto();

        dto.setId(domain.idAsString());
        dto.setName(domain.getName());

        return dto;
    }

    /****************** Status *******************/
    public static StatusEmbedDto createStatusEmbedDto(StatusEmbed domain) {
        StatusEmbedDto dto = new StatusEmbedDto();

        if (domain != null) {
            if (domain.getDate() != null) {
                dto.setDate(domain.getDate().getIsoTime());
            }

            if (domain.getUser() != null) {
                dto.setUser(createUserEmbedDto(domain.getUser()));
            }

            dto.setStatus(domain.getStatus());
            dto.setComment(domain.getComment());
        }

        return dto;
    }

    /*************** Province & District ******************/
    public static CountryDto createCountryDto(Country domain) {
        CountryDto dto = new CountryDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());
        }

        return dto;
    }

    public static CountryDto createCountryDto(CountryEmbed domain) {
        CountryDto dto = new CountryDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());
        }

        return dto;
    }

    public static IdCodeNameDto createCountryIdCodeNameDto(CountryEmbed domain) {
        if (domain != null) {
            IdCodeNameDto dto = new IdCodeNameDto();

            dto.setId(domain.idAsString());
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            return dto;
        }

        return null;
    }

    public static ProvinceDto createProvinceDto(Province domain) {
        ProvinceDto dto = new ProvinceDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            CountryEmbed country = domain.getCountry();
            if (country != null) {
                dto.setCountry(createCountryDto(country));
            }
        }

        return dto;
    }

    public static ProvinceDto createProvinceDto(ProvinceEmbed domain) {
        ProvinceDto dto = new ProvinceDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            CountryEmbed country = domain.getCountry();
            if (country != null) {
                dto.setCountry(createCountryDto(country));
            }
        }

        return dto;
    }

    public static DistrictDto createDistrictDto(District domain) {
        DistrictDto dto = new DistrictDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            ProvinceEmbed province = domain.getProvince();
            if (province != null) {
                dto.setProvince(createProvinceDto(province));
            }
        }

        return dto;
    }

    public static DistrictDto createDistrictDto(DistrictEmbed domain) {
        DistrictDto dto = new DistrictDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            ProvinceEmbed province = domain.getProvince();
            if (province != null) {
                dto.setProvince(createProvinceDto(province));
            }
        }

        return dto;
    }
    
    public static LocationDto createLocationDto(Location domain) {
    	LocationDto dto = new LocationDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            DistrictEmbed district = domain.getDistrict();
            if (district != null) {
                dto.setDistrict(createDistrictDto(district));
            }
        }

        return dto;
    }

    public static LocationDto createLocationDto(LocationEmbed domain) {
    	LocationDto dto = new LocationDto();

        copyPO(domain, dto);

        if (domain != null) {
            dto.setCode(domain.getCode());
            dto.setName(domain.getName());

            DistrictEmbed district = domain.getDistrict();
            if (district != null) {
                dto.setDistrict(createDistrictDto(district));
            }
        }

        return dto;
    }
    
    /*************** Project ******************/
    public static ProjectSimpleDto createProjectSimpleDto(Project domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProjectSimpleDto dto = new ProjectSimpleDto();
    	copyProjectSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, true);
    	
    	return dto;
    }
    
    public static ProjectDto createProjectDto(Project domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProjectDto dto = new ProjectDto();
    	copyProjectSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, false);
        
    	dto.setRequireLand(domain.isRequireLand());
    	dto.setRequireProduct(domain.isRequireProduct());
    	dto.setRequireScientist(domain.isRequireScientist());
    	
        dto.setDescription(domain.getDescription());
        dto.setPosterImage(domain.getPosterImage());
        dto.setAuthorAvatar(authorAvatar);
        
        List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		dto.addImage(image.getName());
        		
        		if (StringUtils.isNullOrEmpty(dto.getMainImage())) {
        			dto.setMainImage(image.getName());
        		}
        		
        		if (StringUtils.isNullOrEmpty(dto.getPosterImage())) {
        			dto.setPosterImage(image.getName());
        		}
        	}
        }
        
        return dto;
    }
    
    public static ProjectCreateDto createProjectCreateDto(Project domain, User currentUser, 
    		boolean canApprove, String format) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProjectCreateDto dto = new ProjectCreateDto();
    	copyBaseItemCreateDto(domain, dto);
    	copyBaseResourceCreateDto(domain.getContact(), dto);
    	
    	dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        dto.setId(domain.idAsString());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null && currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(format));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(format));
        }
        
        dto.setApproveName(domain.getApproveName());
        
        dto.setSquare(domain.getSquare());
        dto.setMoney(domain.getMoney());
        dto.setEmployees(domain.getEmployees());
        
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnitId(domain.getSquareUnit().idAsString());
        }
        
        if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnitId(domain.getMoneyUnit().idAsString());
        }
        
        dto.setClosed(domain.isClosed());
        dto.setRequireLand(domain.isRequireLand());
        dto.setRequireProduct(domain.isRequireProduct());
        dto.setRequireScientist(domain.isRequireScientist());
        
        dto.setAddress(domain.getAddress());
        
        LocationEmbed location = domain.getLocation();
        if (location != null) {
        	dto.setLocationId(location.idAsString());
        	
        	DistrictEmbed district = location.getDistrict();
        	if (district != null) {
        		dto.setDistrictId(district.idAsString());
        		
	        	ProvinceEmbed province = district.getProvince();
	        	if (province != null) {
		        	dto.setProvinceId(province.idAsString());
		        	
		        	CountryEmbed country = province.getCountry();
		        	if (country != null) {
		        		dto.setCountryId(country.idAsString());
		        	}
	        	}
        	}
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategoryId(category.idAsString());
        	}
        }
        
        return dto;
    }
    
    public static LinkProjectDto createLinkProjectDto(Link domain, 
    		Project project, User currentUser, String dateFormat) {
    	if (domain == null || project == null) {
    		return null;
    	}
    	
    	LinkProjectDto dto = new LinkProjectDto();
    	copyProjectSimpleDto(project, currentUser, dto, null, false, dateFormat, false);
    	
    	dto.setLinkId(domain.idAsString());
    	
    	if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        } else {
        	dto.setStatus(StatusEmbedDto.defaulStatus());
        }
    	
        return dto;
    }
    
	/***************Product*****************************/
    public static ProductSimpleDto createProductSimpleDto(Product domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProductSimpleDto dto = new ProductSimpleDto();
    	copyProductSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, true);
    	
    	return dto;
    }
    
    
    public static ProductDto createProductDto(Product domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProductDto dto = new ProductDto();
    	copyProductSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, false);
        
        dto.setDescription(domain.getDescription());
        dto.setPosterImage(domain.getPosterImage());
        dto.setAuthorAvatar(authorAvatar);
        
        List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		dto.addImage(image.getName());
        		
        		if (StringUtils.isNullOrEmpty(dto.getMainImage())) {
        			dto.setMainImage(image.getName());
        		}
        		
        		if (StringUtils.isNullOrEmpty(dto.getPosterImage())) {
        			dto.setPosterImage(image.getName());
        		}
        	}
        }
        
        return dto;
    }
    
    
    public static ProductCreateDto createProductCreateDto(Product domain, 
    		User currentUser, boolean canApprove, String format) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ProductCreateDto dto = new ProductCreateDto();
    	copyBaseItemCreateDto(domain, dto);
    	copyBaseResourceCreateDto(domain.getContact(), dto);
    	
    	dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        dto.setId(domain.idAsString());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());        
        
        dto.setMoneyFrom(domain.getMoneyFrom());
        dto.setMoneyTo(domain.getMoneyTo());
        dto.setPriceFrom(domain.getPriceFrom());
        dto.setPriceTo(domain.getPriceTo());
        dto.setSquare(domain.getSquare());
        dto.setVolume(domain.getVolume());
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(format));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(format));
        }
        
        if (domain.getVolumeUnit() != null) {
        	dto.setVolumeUnitId(domain.getVolumeUnit().idAsString());
        }
        
        if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnitId(domain.getMoneyUnit().idAsString());
        }
        
        if (domain.getPriceUnit() != null) {
        	dto.setPriceUnitId(domain.getPriceUnit().idAsString());
        }
        
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnitId(domain.getSquareUnit().idAsString());
        }
        
        dto.setClosed(domain.isClosed());
        
        dto.setAddress(domain.getAddress());
        
        LocationEmbed location = domain.getLocation();
        if (location != null) {
        	dto.setLocationId(location.idAsString());
        	
        	DistrictEmbed district = location.getDistrict();
        	if (district != null) {
        		dto.setDistrictId(district.idAsString());
        		
	        	ProvinceEmbed province = district.getProvince();
	        	if (province != null) {
		        	dto.setProvinceId(province.idAsString());
		        	
		        	CountryEmbed country = province.getCountry();
		        	if (country != null) {
		        		dto.setCountryId(country.idAsString());
		        	}
	        	}
        	}
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategoryId(category.idAsString());
        	}
        }
        
        List<IdCodeNameEmbed> formCooporations = domain.getFormCooperations();
        if (formCooporations != null) {
        	for (IdCodeNameEmbed category : formCooporations) {
        		dto.addFormCooporateId(category.idAsString());
        	}
        }
        dto.setFormCooporateDiff(domain.getFormCooporateDiff());
        return dto;
    }
    
    public static LinkProductDto createLinkProductDto(Link domain, 
    		Product product, User currentUser, String dateFormat) {
    	if (domain == null || product == null) {
    		return null;
    	}
    	
    	LinkProductDto dto = new LinkProductDto();
    	copyProductSimpleDto(product, currentUser, dto, null, false, dateFormat, false);
    	
    	dto.setLinkId(domain.idAsString());
    	
    	if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        } else {
        	dto.setStatus(StatusEmbedDto.defaulStatus());
        }
    	
        return dto;
    }
    
    /***********************Land****************************************************/
    public static LandSimpleDto createLandSimpleDto(Land domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	LandSimpleDto dto = new LandSimpleDto();
    	copyLandSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, true);
    	
    	return dto;
    }
    
    public static LandDto createLandDto(Land domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	LandDto dto = new LandDto();
    	copyLandSimpleDto(domain, currentUser, dto, authorAvatar, canApprove, dateFormat, false);
        
        dto.setDescription(domain.getDescription());
        dto.setPosterImage(domain.getPosterImage());
        dto.setAuthorAvatar(authorAvatar);
        
        List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		dto.addImage(image.getName());
        		
        		if (StringUtils.isNullOrEmpty(dto.getMainImage())) {
        			dto.setMainImage(image.getName());
        		}
        		
        		if (StringUtils.isNullOrEmpty(dto.getPosterImage())) {
        			dto.setPosterImage(image.getName());
        		}
        	}
        }
        
        return dto;
    }
    
    public static LandCreateDto createLandCreateDto(Land domain, User currentUser, 
    		boolean canApprove, String format) {
    	if (domain == null) {
    		return null;
    	}
    	
    	LandCreateDto dto = new LandCreateDto();
    	copyBaseItemCreateDto(domain, dto);
    	copyBaseResourceCreateDto(domain.getContact(), dto);
    	
    	dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setId(domain.idAsString());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        
        dto.setAnimal(domain.getAnimal());
        dto.setTree(domain.getTree());
        dto.setForest(domain.getForest());
        
        dto.setFormCooporateDiff(domain.getFormCooporateDiff());
        dto.setVolume(domain.getVolume());
        
        List<IdCodeNameEmbed> formCooporations = domain.getFormCooperation();
        if (formCooporations != null) {
        	for (IdCodeNameEmbed category : formCooporations) {
        		dto.addFormCooporateId(category.idAsString());
        	}
        }
        
        List<IdCodeNameEmbed> perposes = domain.getPerposes();
        if (perposes != null) {
        	for (IdCodeNameEmbed category : perposes) {
        		dto.addPerpose(category.idAsString());
        	}
        }
        
        List<IdCodeNameEmbed> nears = domain.getNears();
        if (nears != null) {
        	for (IdCodeNameEmbed category : nears) {
        		dto.addNear(category.idAsString());
        	}
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(format));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(format));
        }
        
        dto.setApproveName(domain.getApproveName());
        dto.setClosed(domain.isClosed());
        
        dto.setSquare(domain.getSquare());
        /*dto.setMoneyFrom(domain.getMoneyFrom());
        dto.setMoneyTo(domain.getMoneyTo());*/
        
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnitId(domain.getSquareUnit().idAsString());
        }
        
        /*if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnitId(domain.getMoneyUnit().idAsString());
        }*/
        
        dto.setAddress(domain.getAddress());
        
        LocationEmbed location = domain.getLocation();
        if (location != null) {
        	dto.setLocationId(location.idAsString());
        	
        	DistrictEmbed district = location.getDistrict();
        	if (district != null) {
        		dto.setDistrictId(district.idAsString());
        		
	        	ProvinceEmbed province = district.getProvince();
	        	if (province != null) {
		        	dto.setProvinceId(province.idAsString());
		        	
		        	CountryEmbed country = province.getCountry();
		        	if (country != null) {
		        		dto.setCountryId(country.idAsString());
		        	}
	        	}
        	}
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategoryId(category.idAsString());
        	}
        }
        
        return dto;
    }
    
    public static LinkLandDto createLinkLandDto(Link domain, 
    		Land land, User currentUser, String dateFormat) {
    	if (domain == null || land == null) {
    		return null;
    	}
    	
    	LinkLandDto dto = new LinkLandDto();
    	copyLandSimpleDto(land, currentUser, dto, null, false, dateFormat, false);
    	
    	dto.setLinkId(domain.idAsString());
    	
    	if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        } else {
        	dto.setStatus(StatusEmbedDto.defaulStatus());
        }
    	
        return dto;
    }
    
    /***********************Article****************************************************/
    public static ArticleSimpleDto createArticleSimpleDto(Article domain, User currentUser, 
    		boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ArticleSimpleDto dto = new ArticleSimpleDto();
    	copyArticleSimpleDto(domain, currentUser, dto, canApprove, dateFormat, true);
    	
    	return dto;
    }
    
    public static ArticleDto createArticleDto(Article domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ArticleDto dto = new ArticleDto();
    	copyArticleSimpleDto(domain, currentUser, dto, canApprove, dateFormat, false);
        
        dto.setDescription(domain.getDescription());
        dto.setPosterImage(domain.getPosterImage());
        dto.setAuthorAvatar(authorAvatar);
        
        List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		dto.addImage(image.getName());
        		
        		if (StringUtils.isNullOrEmpty(dto.getMainImage())) {
        			dto.setMainImage(image.getName());
        		}
        		
        		if (StringUtils.isNullOrEmpty(dto.getPosterImage())) {
        			dto.setPosterImage(image.getName());
        		}
        	}
        }
        
        return dto;
    }
    
    
    public static ArticleCreateDto createArticleCreateDto(Article domain, User currentUser, 
    		boolean canApprove, String format) {
    	if (domain == null) {
    		return null;
    	}
    	
    	ArticleCreateDto dto = new ArticleCreateDto();
    	copyBaseItemCreateDto(domain, dto);
    	//copyBaseResourceCreateDto(domain.getContact(), dto);
    	
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setId(domain.idAsString());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        
        dto.setApproveName(domain.getApproveName());
        dto.setClosed(domain.isClosed());
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategoryId(category.idAsString());
        	}
        }
        
        return dto;
    }
    
    
    /***********************News****************************************************/
    public static NewsSimpleDto createNewsSimpleDto(News domain, User currentUser, 
    		boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	NewsSimpleDto dto = new NewsSimpleDto();
    	copyNewsSimpleDto(domain, currentUser, dto, canApprove, dateFormat, true);
    	
    	return dto;
    }
    
    public static NewsDto createNewsDto(News domain, User currentUser, 
    		String authorAvatar, boolean canApprove, String dateFormat) {
    	if (domain == null) {
    		return null;
    	}
    	
    	NewsDto dto = new NewsDto();
    	copyNewsSimpleDto(domain, currentUser, dto, canApprove, dateFormat, false);
        
        dto.setDescription(domain.getDescription());
        dto.setPosterImage(domain.getPosterImage());
        dto.setAuthorAvatar(authorAvatar);
        
        List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		dto.addImage(image.getName());
        		
        		if (StringUtils.isNullOrEmpty(dto.getMainImage())) {
        			dto.setMainImage(image.getName());
        		}
        		
        		if (StringUtils.isNullOrEmpty(dto.getPosterImage())) {
        			dto.setPosterImage(image.getName());
        		}
        	}
        }
        
        return dto;
    }
    
    public static NewsCreateDto createNewsCreateDto(News domain, User currentUser, 
    		boolean canApprove, String format) {
    	if (domain == null) {
    		return null;
    	}
    	
    	NewsCreateDto dto = new NewsCreateDto();
    	copyBaseItemCreateDto(domain, dto);
    	//copyBaseResourceCreateDto(domain.getContact(), dto);
    	
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null && currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setId(domain.idAsString());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        
        dto.setApproveName(domain.getApproveName());
        dto.setClosed(domain.isClosed());
        
        dto.setAddress(domain.getAddress());
        
        LocationEmbed location = domain.getLocation();
        if (location != null) {
        	dto.setLocationId(location.idAsString());
        	
        	DistrictEmbed district = location.getDistrict();
        	if (district != null) {
        		dto.setDistrictId(district.idAsString());
        		
	        	ProvinceEmbed province = district.getProvince();
	        	if (province != null) {
		        	dto.setProvinceId(province.idAsString());
		        	
		        	CountryEmbed country = province.getCountry();
		        	if (country != null) {
		        		dto.setCountryId(country.idAsString());
		        	}
	        	}
        	}
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategoryId(category.idAsString());
        	}
        }
        
        return dto;
    }	    
    
    /****************************************************************/
    /***************** PUBLIC COPY METHODS **************************/
    /****************************************************************/
    public static void copyPO(PO<ObjectId> domain, DTO<String> dto) {
        if (domain != null && domain.getId() != null) {
            dto.setId(domain.idAsString());
            dto.setActive(domain.isActive());
            // dto.setClientId(domain.clientAsString());
            // dto.setOrgId(domain.orgAsString());
            // dto.setModified(domain.getModified().toString());
        }
    }

    public static void copyPO(POEmbed<ObjectId> domain, DTO<String> dto) {
        if (domain != null && domain.getId() != null) {
            dto.setId(domain.idAsString());
        }
    }

    public static void copyPO(PO<ObjectId> domain, DTOEmbed<String> dto) {
        if (domain != null && domain.getId() != null) {
            dto.setId(domain.idAsString());
        }
    }

    public static void copyPO(POEmbed<ObjectId> domain, DTOEmbed<String> dto) {
        if (domain != null && domain.getId() != null) {
            dto.setId(domain.idAsString());
        }
    }
    
    /****************************************************************/
    /***************** PRIVATE METHODS ******************************/
    /****************************************************************/
    private static void copyBaseItemCreateDto(BaseItem domain, BaseItemCreateDto dto) {
    	List<IdNameEmbed> images = domain.getImages();
        if (images != null) {
        	for (IdNameEmbed image : images) {
        		ImageItemDto imageDto = new ImageItemDto();
        		imageDto.setId(image.idAsString());
        		imageDto.setName(image.getName());
        		imageDto.setDelete(false);
        		
        		//dto.addImage(imageDto);
        	}
        }
        
        dto.setMainImage(domain.getMainImage());
        dto.setPosterImage(domain.getPosterImage());
    }
    
    private static void copyBaseResourceCreateDto(ContactEmbed domain, BaseResourceCreateDto dto) {
    	if (domain == null) {
    		return;
    	}
    	
    	dto.setContactName(domain.getName());
    	dto.setContactAlias(domain.getAlias());
    	
    	dto.setContactPhone(domain.getCompanyPhone());
    	dto.setContactHomePhone(domain.getHomePhone());
    	dto.setContactMobile(domain.getMobilePhone());
    	dto.setContactEmail(domain.getEmail());
    	dto.setContactFax(domain.getFax());
    	
    	dto.setContactAddress(domain.getAddress());
    	
    	LocationEmbed location = domain.getLocation();
    	if (location != null) {
    		dto.setContactLocationId(location.idAsString());
    		
    		DistrictEmbed district = location.getDistrict();
    		if (district != null) {
    			dto.setContactDistrictId(district.idAsString());
    			
    			ProvinceEmbed province = district.getProvince();
    			if (province != null) {
    				dto.setContactProvinceId(province.idAsString());
    				
    				CountryEmbed country = province.getCountry();
    				if (country != null) {
    					dto.setContactCountryId(country.idAsString());
    				}
    			}
    		}
    	}
    }
    
    public static void copyContactDto(User domain, ContactDto dto, String alias) {
    	copyPO(domain, dto);
    	
    	dto.setName(domain.getName());
    	dto.setAlias(alias);
    	dto.setAvatar(domain.getAvatar());
    	
    	dto.setCompanyPhone(domain.getCompanyPhone());
    	dto.setHomePhone(domain.getHomePhone());
    	dto.setMobilePhone(domain.getMobilePhone());
    	dto.setEmail(domain.getEmail());
    	dto.setFax(domain.getFax());
    	dto.setPhone(domain.getMobilePhone());
    	
    	if (StringUtils.isNullOrEmpty(dto.getPhone())) {
    		dto.setPhone(domain.getCompanyPhone());
    		
    		if (StringUtils.isNullOrEmpty(dto.getPhone())) {
    			dto.setPhone(domain.getHomePhone());
    		}
    	}
    	
    	copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
    }
    
    public static void copyContactDto(ContactEmbed domain, ContactDto dto, String avatar) {
    	copyPO(domain, dto);
    	
    	dto.setName(domain.getName());
    	dto.setAlias(domain.getAlias());
    	dto.setAvatar(avatar);
    	
    	dto.setCompanyPhone(domain.getCompanyPhone());
    	dto.setHomePhone(domain.getHomePhone());
    	dto.setMobilePhone(domain.getMobilePhone());
    	dto.setEmail(domain.getEmail());
    	dto.setFax(domain.getFax());
    	dto.setPhone(domain.getMobilePhone());
    	
    	if (StringUtils.isNullOrEmpty(dto.getPhone())) {
    		dto.setPhone(domain.getCompanyPhone());
    		
    		if (StringUtils.isNullOrEmpty(dto.getPhone())) {
    			dto.setPhone(domain.getHomePhone());
    		}
    	}
    	
    	copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
    }
    
    /*****************************************************************************************/
    
    /***************************Project***********************************************************/
    private static void copyLocationAddressDto(LocationAddressDto dto, String address, LocationEmbed location) {
    	dto.setAddress(address);
    	
    	if (location != null) {
    		dto.setLocation(createLocationDto(location));
    	}
    	
    	dto.setFullAddress(AddressUtils.getFullAddress(address, location));
    }
    
    private static void copyProjectSimpleDto(Project domain, User currentUser, 
    		ProjectSimpleDto dto, String authorAvatar, boolean canApprove, 
    		String dateFormat, boolean createTitle) {
    	copyPO(domain, dto);
    	copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
    	
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setName(domain.getName());
        
        ContactEmbed contact = domain.getContact();
		if (contact != null) {
        	ContactDto contactDto = new ContactDto();
        	DomainConverter.copyContactDto(contact, contactDto, authorAvatar);
        	
        	dto.setContact(contactDto);
        }
        
        if (createTitle) {
	        String description = Jsoup.parse(domain.getDescription()).text();
	        String title = StringUtils.substringWords(description, ConstantUtils.PROJECT_TITLE_LENGTH, " "); 
	        dto.setTitle(title);
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(dateFormat));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(dateFormat));
        }
        
        dto.setApproveName(domain.getApproveName());
        dto.setMainImage(domain.getMainImage());
        dto.setSquare(domain.getSquare());
        
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnit(createIdCodeNameDto(domain.getSquareUnit()));
        }
        
        dto.setMoney(domain.getMoney());
        
        if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnit(createIdCodeNameDto(domain.getMoneyUnit()));
        }
        
        dto.setEmployees(domain.getEmployees());
        dto.setClosed(domain.isClosed());
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategory(createIdCodeNameDto(category));
        	}
        }
        
        if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        }
    }
	
    /*********************************copyProductSimpleDto*******************************************/
	private static void copyProductSimpleDto(Product domain, User currentUser, 
    		ProductSimpleDto dto, String ownerAvatar, boolean canApprove, 
    		String dateFormat, boolean createTitle) {
    	copyPO(domain, dto);
        copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
        
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setName(domain.getName());
        
        ContactEmbed contact = domain.getContact();
		if (contact != null) {
        	ContactDto contactDto = new ContactDto();
        	DomainConverter.copyContactDto(contact, contactDto, ownerAvatar);
        	
        	dto.setContact(contactDto);
        }
        
        if (createTitle) {
	        String description = Jsoup.parse(domain.getDescription()).text();
	        String title = StringUtils.substringWords(description, ConstantUtils.PRODUCT_TITLE_LENGTH, " "); 
	        dto.setTitle(title);
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(dateFormat));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(dateFormat));
        }
        
        dto.setApproveName(domain.getApproveName());
        dto.setMainImage(domain.getMainImage());
        dto.setVolume(domain.getVolume());
        dto.setClosed(domain.isClosed());
        
        if (domain.getVolumeUnit() != null) {
        	dto.setVolumeUnit(createIdCodeNameDto(domain.getVolumeUnit()));
        }
        
        dto.setFormCooporateDiff(dto.getFormCooporateDiff());        
        List<IdCodeNameEmbed> formCooporations = domain.getFormCooperations();
        if (formCooporations != null) {
        	for (IdCodeNameEmbed category : formCooporations) {
        		dto.addFormCooporations(createIdCodeNameDto(category));
        	}
        }
        
        dto.setMoneyFrom(domain.getMoneyFrom());
        dto.setMoneyTo(domain.getMoneyTo());        
        
        if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnit(createIdCodeNameDto(domain.getMoneyUnit()));
        }
        
        dto.setPriceFrom(domain.getPriceFrom());
        dto.setPriceTo(domain.getPriceTo());        
        
        if (domain.getPriceUnit() != null) {
        	dto.setPriceUnit(createIdCodeNameDto(domain.getPriceUnit()));
        }
        
        dto.setSquare(domain.getSquare());
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnit(createIdCodeNameDto(domain.getSquareUnit()));
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategory(createIdCodeNameDto(category));
        	}
        }
        
        if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        }
        
        dto.setFormCooporateDiff(domain.getFormCooporateDiff());
    }
	
	/*****************************Land copy simple dto***************************************/
    private static void copyLandSimpleDto(Land domain, User currentUser, 
    		LandSimpleDto dto, String authorAvatar, boolean canApprove, 
    		String dateFormat, boolean createTitle) {
    	copyPO(domain, dto);
    	copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
    	
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null 
        			&& currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setName(domain.getName());
        dto.setApproveName(domain.getApproveName());
        
        ContactEmbed contact = domain.getContact();
		if (contact != null) {
        	ContactDto contactDto = new ContactDto();
        	DomainConverter.copyContactDto(contact, contactDto, authorAvatar);
        	
        	dto.setContact(contactDto);
        }
        
        if (createTitle) {
	        String description = Jsoup.parse(domain.getDescription()).text();
	        String title = StringUtils.substringWords(description, ConstantUtils.LAND_TITLE_LENGTH, " "); 
	        dto.setTitle(title);
        }
        
        if (domain.getStartDate() != null) {
        	dto.setStartDate(domain.getStartDate().format(dateFormat));
        }
        
        if (domain.getEndDate() != null) {
        	dto.setEndDate(domain.getEndDate().format(dateFormat));
        }
        
        dto.setApproveName(domain.getApproveName());
        dto.setMainImage(domain.getMainImage());
        dto.setClosed(domain.isClosed());
        
        dto.setAnimal(domain.getAnimal());
        dto.setTree(domain.getTree());
        dto.setForest(domain.getForest());
        dto.setVolume(domain.getVolume());
        
        dto.setSquare(domain.getSquare());
        
        if (domain.getSquareUnit() != null) {
        	dto.setSquareUnit(createIdCodeNameDto(domain.getSquareUnit()));
        }
        
        /*dto.setMoneyFrom(domain.getMoneyFrom());
        dto.setMoneyTo(domain.getMoneyTo());
        
        if (domain.getMoneyUnit() != null) {
        	dto.setMoneyUnit(createIdCodeNameDto(domain.getMoneyUnit()));
        }*/
        
        dto.setFormCooporateDiff(dto.getFormCooporateDiff());
        
        List<IdCodeNameEmbed> formCooporations = domain.getFormCooperation();
        if (formCooporations != null) {
        	for (IdCodeNameEmbed category : formCooporations) {
        		dto.addFormCooporations(createIdCodeNameDto(category));
        	}
        }
        
        List<IdCodeNameEmbed> nears = domain.getNears();
        if (nears != null) {
        	for (IdCodeNameEmbed category : nears) {
        		dto.addNear(createIdCodeNameDto(category));
        	}
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategory(createIdCodeNameDto(category));
        	}
        }
        
        if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        }
    }
    
    /************************************copyArticleSimpleDto******************************/
    private static void copyArticleSimpleDto(Article domain, User currentUser, 
    		ArticleSimpleDto dto, boolean canApprove, String dateFormat, boolean createTitle) {
    	copyPO(domain, dto);
        
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null && currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setName(domain.getName());
        
        if (domain.getAuthor() != null) {
        	dto.setAuthor(createIdNameDto(domain.getAuthor()));
        }
        
        if (createTitle) {
	        String description = Jsoup.parse(domain.getDescription()).text();
	        String title = StringUtils.substringWords(description, ConstantUtils.ARTICLE_TITLE_LENGTH, " "); 
	        dto.setTitle(title);
        }
                
        dto.setApproveName(domain.getApproveName());
        dto.setMainImage(domain.getMainImage());
        dto.setClosed(domain.isClosed());
        
        if (domain.getCreateDate() != null) {
        	dto.setCreateDate(domain.getCreateDate().format(dateFormat));
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategory(createIdCodeNameDto(category));
        	}
        }
        
        if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        }
    }
    
    /************************************News******************************/
    private static void copyNewsSimpleDto(News domain, User currentUser, 
    		NewsSimpleDto dto, boolean canApprove, String dateFormat, boolean createTitle) {
    	copyPO(domain, dto);
        copyLocationAddressDto(dto, domain.getAddress(), domain.getLocation());
        
        dto.setOwner(false);
        dto.setCanApprove(canApprove);
        
        if (currentUser != null) {
        	IUser<ObjectId> createdBy = domain.getCreatedBy();
        	
        	if (createdBy != null && currentUser.getId().equals(createdBy.getId())) {
        		dto.setOwner(true);
        	}
        }
        
        dto.setName(domain.getName());
        dto.setApproveName(domain.getApproveName());
        dto.setMainImage(domain.getMainImage());
        dto.setClosed(domain.isClosed());
        
        if (domain.getAuthor() != null) {
        	dto.setAuthor(createIdNameDto(domain.getAuthor()));
        }
        
        if (createTitle) {
	        String description = Jsoup.parse(domain.getDescription()).text();
	        String title = StringUtils.substringWords(description, ConstantUtils.NEWS_TITLE_LENGTH, " "); 
	        dto.setTitle(title);
        }
        
        List<IdCodeNameEmbed> categories = domain.getCategories();
        if (categories != null) {
        	for (IdCodeNameEmbed category : categories) {
        		dto.addCategory(createIdCodeNameDto(category));
        	}
        }
        
        if (domain.getStatus() != null) {
        	dto.setStatus(createStatusEmbedDto(domain.getStatus()));
        }
    }
}
