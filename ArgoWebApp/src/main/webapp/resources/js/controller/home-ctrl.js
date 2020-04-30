app.controller('HomeController', ['$scope', function($scope) {
	
	$scope.$on('ngLastRepeat.HomePage.Project', function (event, data) {
		$('#projectCarousel.multi-item-carousel .item').each(function(){
			  var next = $(this).next();
			  if (!next.length) {
			    next = $(this).siblings(':first');
			  }
			  next.children(':first-child').clone().appendTo($(this));
			  
			  if (next.next().length>0) {
			    next.next().children(':first-child').clone().appendTo($(this));
			  } else {
			  	$(this).siblings(':first').children(':first-child').clone().appendTo($(this));
			  }
			});
	});
	
        $scope.templates =
            [
                { name: 'main.html', url: 'resources/view/main.html'},
                { name: 'company.html', url: 'resources/view/company.html'},
                { name: 'farmer.html', url: 'resources/view/farmer.html'},
                { name: 'scientist.html', url: 'resources/view/scientist.html'},
                { name: 'administrator.html', url: 'resources/view/administrator.html'},

            ];
        $scope.template = $scope.templates[0];

        $scope.header = 'resources/view/header.html';
        $scope.footer = 'resources/view/footer.html';

        $scope.onClickMenu = function( index ){
            if(index >=0 && index < 5){
                $scope.template = $scope.templates[index];
            }
        }
        
        $scope.searchTypes = [
                { value: 'ENTERPRISE', name: 'Doanh nghiệp' },
                { value: 'SCIENTIST', name: 'Nhà khoa học' },
                { value: 'FARMER', name: 'Nông dân' },
                { value: 'LAND', name: 'Thửa đất' },
                { value: 'PRODUCT', name: 'Nông sản' },
                { value: 'NEWS', name: 'Tin chính quyền' }
	    ];
        
        $scope.districts = [
	            {code:"001", name:"Thành Phố Cao Bằng"},
	            {code:"002", name:"Bảo Lạc"},
	            {code:"003", name:"Bảo Lâm"},
	            {code:"004", name:"Hạ Lang"},
	            {code:"005", name:"Hà Quảng"},
	            {code:"006", name:"Hòa An"},
	            {code:"007", name:"Nguyên Bình"},
	            {code:"008", name:"Phục Hòa"},
	            {code:"009", name:"Quảng Uyên"},
	            {code:"010", name:"Thạch An"},
	            {code:"011", name:"Thông Nông"},
	            {code:"012", name:"Trà Lĩnh"},
	            {code:"013", name:"Trùng Khánh"}
            ]
        
        $scope.hightlightProducts = [
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-20.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-4.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-6.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-19.jpg', des:''},
		];

        $scope.products = [
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-20.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-4.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-6.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-19.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-20.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-2.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-6.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-7.jpg', des:''},
            {name:'Nho my Bao Loc', nsx:'Huyen Bao Loc', package:'', contact:'Vu Tra Pham', img:'/agro/resources/images/agri-9.jpg', des:''}

        ];

        $scope.lands = [
            {name:'Đất Nông Nghiệp tại số 19 Xã Cao Thăng', area:'46', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/land-1.jpg' },
            {name:'Đất Chăn Nuôi tại số 77 Xã Độc Lập', area:'100', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/land-2.jpg' },
            {name:'Đất Chăn Nuôi tại số 93 Xã Vị Quang', area:'46', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/land-3.jpg' },
            {name:'Dat chan nuoi tai so 77 xa Doc Lap', area:'46', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/land-4.jpg' }
        ]


        $scope.projects = [
            {name:'Dự án nuôi cá nước ngọt', field:'Thuy san', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/project-1.jpg' },
            {name:'Dự án nuôi bò sữa', field:'Gia suc', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/project-2.jpg' },
            {name:'Phủ xanh đất trống đồi trọc', field:'Cay trong', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/project-3.jpg' },
            {name:'Dự án nuôi heo thịt', field:'Cay trong', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/project-4.jpg' },
            {name:'Phát triển vùng nông nghiệp mía đường', field:'Cay trong', location:'So 90 xa Hung Dao', contact:'Pham Quang Phat', des:'',img:'/agro/resources/images/project-4.jpg' }
        ]


        $scope.scientists = [
            {
                name:'Nuoi ca nuo ngot',
                field:'Thuy san',
                author:'Pham Quang Phat',
                des:'Theo b�o c�o c?a Chi c?c Tr?ng tr?t - BVTV Ngh? An, t? cu?i th�ng 6 ??n n?a ??u th�ng 8/2017, di?n t�ch l�a b? b?nh l�n s?c ?en t?ng t? 54 ha l�n 675,5 ha.',
                dop:'09-10-2017 16:04:00',
                img:'/agro/resources/images/s1.jpeg'
            },
            {
                name:'Nuoi ca nuo ngot',
                field:'Thuy san',
                author:'Pham Quang Phat',
                des:'Theo b�o c�o c?a Chi c?c Tr?ng tr?t - BVTV Ngh? An, t? cu?i th�ng 6 ??n n?a ??u th�ng 8/2017, di?n t�ch l�a b? b?nh l�n s?c ?en t?ng t? 54 ha l�n 675,5 ha.',
                dop:'09-10-2017 10:09:34',
                img:'/agro/resources/images/s1.jpeg'
            },
            {
                name:'Nuoi ca nuo ngot',
                field:'Thuy san',
                author:'Pham Quang Phat',
                des:'Theo b�o c�o c?a Chi c?c Tr?ng tr?t - BVTV Ngh? An, t? cu?i th�ng 6 ??n n?a ??u th�ng 8/2017, di?n t�ch l�a b? b?nh l�n s?c ?en t?ng t? 54 ha l�n 675,5 ha.',
                dop:'09-10-2017 09:23:09',
                img:'/agro/resources/images/s1.jpeg'
            },
            {
                name:'Nuoi ca nuo ngot',
                field:'Thuy san',
                author:'Pham Quang Phat',
                des:'Theo b�o c�o c?a Chi c?c Tr?ng tr?t - BVTV Ngh? An, t? cu?i th�ng 6 ??n n?a ??u th�ng 8/2017, di?n t�ch l�a b? b?nh l�n s?c ?en t?ng t? 54 ha l�n 675,5 ha.',
                dop:'09-10-2017 07:08:03',
                img:'/agro/resources/images/s1.jpeg'
            }
        ]

    }]);