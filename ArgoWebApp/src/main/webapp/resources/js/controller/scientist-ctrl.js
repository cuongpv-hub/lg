app.controller('ScientistController', ['$scope', function($scope) {
	
	
	$scope.scientists = [ 
	                      {
	                    	  id:"1", 
	                    	  code:"001", 
	                    	  name:"Phạm Ngọc Hùng", 
	                    	  sex:"1",
	                    	  dayOfBirth:"09/02/1979",
	                    	  address:{detail:"Thanh Sơn, Kim Bảng, Hà Nam"},
	                    	  contact:{phone:"0421 898 654"},
	                    	  titile:"P.GS", 
	                    	  position:"Trưởng bộ môn CNPM",
	                    	  workplace:"Trường ĐH Công Nghệ - ĐH Quốc Gia Hà Nội",
	                    	  scientistFields:[{code:"001", name:"Chăn nuôi gia cầm"},{code:"002", name:"Thủy sản"}],
	                    	  scientistMajors:[{code:"001", name:"Chăn nuôi"}],
	                    	  imgs:["../resources/images/12.jpg"]
	                      },
	                      
	                      {
	                    	  id:"2", 
	                    	  code:"002", 
	                    	  name:"Trương Nam Thành", 	                    	  
	                    	  sex:"1",
	                    	  dayOfBirth:"09/02/1964",
	                    	  address:{detail:"Số 4A,94 - Hồ Tùng Mậu, Mai Dịch, Cầu Giấy, Hà Nội"},
	                    	  contact:{phone:"0421 898 654"},
	                    	  titile:"P.GS", 
	                    	  position:"Trưởng bộ môn KHMT",
	                    	  workplace:"Trường ĐH Công Nghệ - ĐH Quốc Gia Hà Nội",
	                    	  scientistFields:[{code:"001", name:"Chăn nuôi gia cầm"},{code:"002", name:"Thủy sản"}],
	                    	  scientistMajors:[{code:"001", name:"Chăn nuôi"}],
	                    	  imgs:["../resources/images/7.jpg"]
	                      }
	                      
	                      
	                      ]
	
	
	
	
	$scope.papers = [{
			id:"001",
			name:"Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa",
			titile:"Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa",
			abstr:"Phân khúc tầm trung đang nóng lên hơn bao giờ hết khi vào tháng 5 tới, Samsung sẽ trình làng hai tân binh mới là Galaxy A6 & Galaxy A6+ tại thị trường Việt Nam.",
			content:"Phân khúc tầm trung đang nóng lên hơn bao giờ hết khi vào tháng 5 tới, Samsung sẽ trình làng hai tân binh mới là Galaxy A6 & Galaxy A6+ tại thị trường Việt Nam.",
			scientist:$scope.scientists[0],
			approve:1,
			imgs:["../resources/images/agr-tomato-1.png"]
		},
		{
			id:"002",
			name:"Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa",
			titile:"Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa",
			abstr:"Phân khúc tầm trung đang nóng lên hơn bao giờ hết khi vào tháng 5 tới, Samsung sẽ trình làng hai tân binh mới là Galaxy A6 & Galaxy A6+ tại thị trường Việt Nam.",
			content:"Phân khúc tầm trung đang nóng lên hơn bao giờ hết khi vào tháng 5 tới, Samsung sẽ trình làng hai tân binh mới là Galaxy A6 & Galaxy A6+ tại thị trường Việt Nam.",
			scientist:$scope.scientists[1],
			approve:1,
			imgs:["../resources/images/contact_us.png"]
		}
	                 ]
	
	
        
        
        

    }]);