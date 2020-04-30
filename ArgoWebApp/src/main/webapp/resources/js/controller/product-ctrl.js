app.controller('ProductController', ['$scope', function($scope) {
	$scope.global = {
			advanceSearch: false
	};
	
	$scope.imageFiles = [{}];
	
	$scope.toggleSearch = function() {
		$scope.global.advanceSearch = !$scope.global.advanceSearch;
	}
	
	$scope.addImageFile = function() {
		$scope.imageFiles.push({});
	}
	
}]);