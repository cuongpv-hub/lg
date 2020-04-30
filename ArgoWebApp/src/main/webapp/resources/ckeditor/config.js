/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'about' }
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
	
	//config.extraPlugins = 'simpleuploads';
	config.startupFocus = false;
    if(isIE()){
        if (!window.location.origin) {
            window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
        }
        
        config.filebrowserBrowseUrl = CKEDITOR_IMAGE_MANAGER_BASE_URL + 'browse';
        config.filebrowserUploadUrl = CKEDITOR_IMAGE_MANAGER_BASE_URL + 'upload-classic';
    } else {
        config.filebrowserBrowseUrl = CKEDITOR_IMAGE_MANAGER_BASE_URL + 'browse';
        config.filebrowserUploadUrl = CKEDITOR_IMAGE_MANAGER_BASE_URL + 'upload';
    }
    
    config.filebrowserWindowWidth = '640';
    config.filebrowserWindowHeight = '480';
    config.selectMultiple = false;
};

function isIE() {
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");
    return !!(msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./));
}