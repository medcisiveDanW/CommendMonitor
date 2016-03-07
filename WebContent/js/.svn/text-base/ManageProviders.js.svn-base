var ManageProviders = function(targetId) {
    var self = this;

    (function() {
        self.targetId = targetId;
        _init();
    })(jQuery);

    return self;

    function _init() {
        var block =
            '<div id="managerProvidersTabsDivId" class="">'
            + '     <ul>'
            + '         <li><a href="#addProviderDivId">Add Provider</a></li>'
            + '         <li><a href="#addClinicDivId">Add Clinics to Provider</a></li>'
            + '         <li><a href="#editProviderDivId">Edit Provider</a></li>'
            + '     </ul>'
            + '     <div id="addProviderDivId">Error if you can read this!</div>'
            + '     <div id="addClinicDivId">Error if you can read this!</div>'
            + '     <div id="editProviderDivId">Error if you can read this!</div>'
            + '</div>';
        $('#' + self.targetId).html(block);
        $('#managerProvidersTabsDivId').tabs();
        var clinicPtr = new AddClinic('addClinicDivId');
        var hook = function(obj) {
            clinicPtr.addClinicsToProvider(obj);
            $('#managerProvidersTabsDivId').tabs({selected: 1});
        }
        new AddProvider('addProviderDivId',hook);
        new EditProvider('editProviderDivId');
    }
};