app.controller('CompanyController', ['$scope', function($scope) {
         
        
	$scope.news = [{
		code:"001",
		name:"Giới thiệu tỉnh cao bằng",
		dayPost:"02-11-2017",
		abtrs:"Cao Bằng là tỉnh nằm ở phía Đông Bắc Việt Nam. Hai mặt Bắc và Đông Bắc giáp với tỉnh Quảng Tây (Trun...",
		content:""
		
	}]
	
        $scope.companys = [
                           {
                        	   name:"Công ty TNHH Thanh Lâm", 
                        	   address:"1, Ấp 0, Phường Lục Thắm Hợp, Huyện Bùi, Hưng Yênn",
                        	   fields:["Trồng rừng", "Xây dựng"],
                        	   phone:"84-73-952-4711",
                        	   who:"Tống Trà Ông",
                        	   imgs:["resources/images/org-1.jpg", "resources/images/org-2.jpg"]
                           },
                           {
                        	   name:"Công ty TNHH Thanh Lâm", 
                        	   address:"1, Ấp 0, Phường Lục Thắm Hợp, Huyện Bùi, Hưng Yênn",
                        	   fields:["Trồng rừng", "Xây dựng"],
                        	   phone:"84-73-952-4711",
                        	   who:"Tống Trà Ông",
                        	   imgs:["resources/images/org-1.jpg", "resources/images/org-2.jpg"]
                           }
                           
                           ]
        
        $scope.projects = [
                           {
                        	   name:"Dự án  Trồng Chuối Thần Tiên CB-313",
                        	   company:$scope.companys[0],
                        	   phone:"(093)417-6843",
                        	   address:"Xóm 2 Thanh Sơn - xã Tân Sơn - huyện Bảo Lạc tỉnh Cao Bằng",
                        	   description:"Hiện Công ty TNHH Thanh Lâm đang có 3 trang trại nuôi tôm trong nhà ở Thừa Thiên-Huế với diện tích lên đến 215 ha theo hình thức siêu thâm canh. Ông Yuttana Thon...",
                        	   imgs:["resources/images/project-7.jpg"]
                        		   
                        		   
                           },
                           {
                        	   name:"Dự án  Trồng Chuối Thần Tiên CB-313",
                        	   company:$scope.companys[0],
                        	   phone:"(093)417-6843",
                        	   address:"Xóm 2 Thanh Sơn - xã Tân Sơn - huyện Bảo Lạc tỉnh Cao Bằng",
                        	   description:"Hiện Công ty TNHH Thanh Lâm đang có 3 trang trại nuôi tôm trong nhà ở Thừa Thiên-Huế với diện tích lên đến 215 ha theo hình thức siêu thâm canh. Ông Yuttana Thon...",
                        	   imgs:["resources/images/project-7.jpg"]
                        		   
                        		   
                           }
        ] 

    }]);