app.controller('FarmerController', ['$scope', function($scope) {
         
        
        
        $scope.farmers = [
                           {
                        	   id:"1",name:"Phạm Quang Hải",sex:1,birthDay:"21/09/1986", 
                        	   address:{
                        		   detail:"Xóm 2 - Phù Vân, xã Tân Lạc, huyện Quảng Uyên, tỉnh Cao Bằng"
                        	   },
                        	   contact:{
                        		   phone:"0321 365 478",
                        		   mobile:"0972 826 758",
                        		   email:"haipq@gmail.com"
                        	   },
                        	   imgs:["../resources/images/1.jpg","../resources/images/2.jpg"]
                        	   
                           
                           },
                           {
                        	   id:"2",name:"Trần Bích Hồng",sex:1,birthDay:"11/03/1983", 
                        	   address:{
                        		   detail:"Xóm 2 - Châu Giang, phường Minh Khai, thành phố Cao Bằng, tỉnh Cao Bằng"
                        	   },
                        	   contact:{
                        		   phone:"0321 365 478",
                        		   mobile:"0972 826 758",
                        		   email:"hongbh@gmail.com"
                        	   }, 
                        	   imgs:["../resources/images/3.jpg","../resources/images/4.jpg"]
                           }
                           
                           ]
        
        $scope.lands = [
                           {
                        	   id:"1", code:"LAND-1001", name:"Đất trồng dừng", square:25, squareUnit:"ha",
                        	   isHighway:"true", isIrrigationSystem:"false", isGroundWater:"true", 
                        	   landType:{code:"001", name:"Đất rừng"},
                        	   landPerpose:{code:"001",name:"Đất trồng cây lâu năm"},
                        	   status:"0",
                        	   statusName:"Đất chưa liên kết",
                        	   who:$scope.farmers[0],
                        	   imgs:["../resources/images/land-1.jpg","../resources/images/land-2.jpg"],
                        	   description:"Tôi muốn bán thửa đất thổ cư SĐCC ở ngõ 79, phường Quan Hà. Diện tích 45m2. Mặt tiền 4m, nở hậu 4.1m, chiều dài 11.2m, hướng Đông Nam, ô tô đỗ cửa, rấ..."
                        		   
                        		   
                           },
                           {
                        	   
                        	   id:"1", code:"LAND-1002", name:"Đất nông nghiệp", square:25, squareUnit:"m2",
                        	   isHighway:"true", isIrrigationSystem:"true", isGroundWater:"true", 
                        	   landType:{code:"001", name:"Đất nông nghiệp"},
                        	   landPerpose:{code:"001",name:"Nuôi trồng thủy sản"},
                        	   status:"0",
                        	   statusName:"Đất chưa liên kết",
                        	   who:$scope.farmers[1],
                        	   imgs:["../resources/images/land-3.jpg","../resources/images/land-4.jpg"],
                        	   description:"Tôi muốn bán thửa đất thổ cư SĐCC ở ngõ 79, phường Quan Hà. Diện tích 45m2. Mặt tiền 4m, nở hậu 4.1m, chiều dài 11.2m, hướng Đông Nam, ô tô đỗ cửa, rấ..."
                        		   
                           }
        ] 
        
        
        $scope.products = [
                        {
                     	   id:"1",code:"001",name:"Bưởi Diễn",description:"Bưởi diễn cho chất lượng tốt, nguồn dinh dưỡng cao",
                     	   price:"35K",
                     	   priceUnit:"VNĐ",
                     	   who:$scope.farmers[0],                     	   
                     	   imgs:["",""],
                     	   status:"0",
                     	   statusName:"Nông sản chưa liên kết",
                           productType:{code:"001", name:"Nông sản"},
                        	imgs:["../resources/images/agri-1.jpg","../resources/images/agri-2.jpg"]
                        },
                        {
                     	   
                        	id:"2",code:"002",name:"Lúa vụ thu đông",description:"Lúa nếp lương, chất lượng cao, giá cả phải chăng",
                      	   	price:"860K",
                      	   	priceUnit:"VNĐ",
                      	   	who:$scope.farmers[0],   
                      	   	status:"0",
                      	   	statusName:"Nông sản chưa liên kết",
                            productType:{code:"001", name:"Nông sản"}   ,
                            imgs:["../resources/images/agri-3.jpg","../resources/images/agri-4.jpg"]                     		   
                        }
     ] 

    }]);