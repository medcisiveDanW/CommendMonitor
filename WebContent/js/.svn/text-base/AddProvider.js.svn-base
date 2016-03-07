var AddProvider = function(targetId,hook) {
    var self = this;

    (function() {
        self.targetId = targetId,
        self.hook = hook,
        self.u = new Utility();
        if(self.hook==undefined) { self.hook = function(){}; }
        _init();
    })(jQuery);

    return self;

    function _init() {
        var block =
        '<div class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em; margin-top: 7px;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 1: Enter Providers Name and Sta3n.</strong>'
        + '             <input id="addProviderNameInputId" type="text" value="Providers Name"/>'
        + '             <input id="addProviderSta3nInputId" type="text" value="Providers Sta3n"/>'
        + '             <input id="addProviderSearchButtonId" class="buttonClass" type="button" value="Search"/>'
        + '         </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).html(block);
        $('.buttonClass').button();
        $('#addProviderSearchButtonId').click(function() {
            var providerName = $('#addProviderNameInputId').val(),
                sta3n = $('#addProviderSta3nInputId').val();
            _addProviderTable(providerName,sta3n);
        });
        $('#addProviderNameInputId').click(function() {$('#addProviderNameInputId').val('');});
        $('#addProviderNameInputId').blur(function() {
            var val = $('#addProviderNameInputId').val();
            if(val=='') {
                $('#addProviderNameInputId').val('Providers Name');
            } else {}
        });
        $('#addProviderSta3nInputId').click(function() {$('#addProviderSta3nInputId').val('');});
        $('#addProviderSta3nInputId').blur(function() {
            var val = $('#addProviderSta3nInputId').val();
            if(val=='') {
                $('#addProviderSta3nInputId').val('Providers Sta3n');
            } else {}
        });
    }

    function _addProviderTable(providerName,sta3n) {
        $('#addProvidersTableWidgetId').remove();
        var providers = null;
        $.ajax({
            type:   "post",
            url:    "ManagmentServlet",
            data:   "option=getExternalProviders&ProviderName=" + providerName + "&Sta3n=" + sta3n,
            async:  false,
            success: function(msg) {
                providers = msg._table;
            },
            error: function(xhr, textStatus, errorThrown) {
                self.u.print(xhr);
                self.u.print(textStatus);
                self.u.print(errorThrown);
                //window.location = 'login.html?error=' + errorThrown;
            }
        });
        var module =
            '<table id="addProviderTableId" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
                '<thead>' +
                    '<tr>' +
                        '<th>Sta3n</th>' +
                        '<th>ProviderIEN</th>' +
                        '<th>ProviderSID</th>' +
                        '<th>ProviderName</th>' +
                        '<th>Select</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>';
            for(i in providers) {
                var cur = providers[i];
                module +=
                    '<tr class="gradeC">' +
                        '<td>' + cur.Sta3n + '</td>' +
                        '<td>' + cur.ProviderIEN + '</td>' +
                        '<td>' + cur.ProviderSID + '</td>' +
                        '<td>' + cur.ProviderName + '</td>' +
                        '<td class="center"><img id="addProviderSelectionImgId' + i + '" class="icon-class" src="images/add.png" alt="Select"/></td>' +
                    '</tr>';
            }
        module +=
                '</tbody>' +
            '</table>';

        var block =
        '<div id="addProvidersTableWidgetId" class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 2: Select Provider to add.</strong>'
        + module
        + '        </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).append(block);
        $('#addProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        for(i in providers) {
            $('#addProviderSelectionImgId' + i).click(function() {
                var d = providers[this.id.replace("addProviderSelectionImgId","")];
                $.ajax({
                    type:   "post",
                    url:    "ManagmentServlet",
                    data:   "option=addProvider&Sta3n=" + d.Sta3n + "&ProviderIEN=" + d.ProviderIEN + "&ProviderSID=" + d.ProviderSID + "&ProviderName=" + d.ProviderName,
                    async:  false,
                    success: function(msg) {
                        alert(d.ProviderName + " has been added to the provider group");
                        var transformedProvider = {'sta3n': d.Sta3n, 'DUZ': d.ProviderIEN, 'SID': d.ProviderSID, 'name': d.ProviderName};
                        self.hook(transformedProvider);
                    },
                    error: function(xhr, textStatus, errorThrown) {
                        self.u.print(xhr);
                        self.u.print(textStatus);
                        self.u.print(errorThrown);
                        alert("Could not save providers.  Please contact tech suport.");
                    }
                });
            });
        }
    }
};