var AddCosigner = function(editProviderObj) {
    var self = this;

    (function() {
        self.parent = editProviderObj,
        self.provider = null,
        self.u = new Utility();
        if(self.hook==undefined) { self.hook = function(){}; }
    })(jQuery);

    self.init = function(providerObj) {
        self.provider = providerObj;
        var block =
        '<div class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em; margin-top: 7px;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>Provider ' + self.provider.name + '</strong><br>'
        + '        <strong>STEP 1: Enter Cosigners Name.</strong>'
        + '             <input id="addCosignerNameInputId" type="text" value="Cosigners Name"/>'
        + '             <input id="addCosignerSearchButtonId" class="buttonClass" type="button" value="Search"/>'
        + '             <input id="addCosignerClearSelectedButtonId" class="buttonClass" type="button" value="Clear Selected Provider"/>'
        + '         </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.parent.targetId).html(block);
        self.u.print(self.parent.targetId);
        $('.buttonClass').button();
        $('#addCosignerClearSelectedButtonId').click(function() {
            self.parent.reset();
        });
        $('#addCosignerSearchButtonId').click(function() {
            var cosignerName = $('#addCosignerNameInputId').val();
            _cosignerTable(cosignerName);
        });
        $('#addCosignerNameInputId').click(function() {$('#addCosignerNameInputId').val('');});
        $('#addCosignerNameInputId').blur(function() {
            var val = $('#addCosignerNameInputId').val();
            if(val=='') {
                $('#addCosignerNameInputId').val('Cosigners Name');
            } else {}
        });
    }

    return self;

    function _cosignerTable(cosignerName) {
        $('#addProvidersTableWidgetId').remove();
        var data = null;
        $.ajax({
            type:   "post",
            url:    "ManagmentServlet",
            data:   "option=getExternalProviders&ProviderName=" + cosignerName + "&Sta3n=" + self.provider.sta3n,
            async:  false,
            success: function(msg) {
                data = msg._table;
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
            for(i in data) {
                var cur = data[i];
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
        $('#' + self.parent.targetId).append(block);
        $('#addProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        for(i in data) {
            $('#addProviderSelectionImgId' + i).click(function() {
                var d = data[this.id.replace("addProviderSelectionImgId","")];
                self.u.print(d);
                $.ajax({
                    type:   "post",
                    url:    "ManagmentServlet",
                    data:   "option=addCosigner&Sta3n=" + self.provider.sta3n + "&ProviderSID=" + self.provider.SID + "&CosignerIEN=" + d.ProviderIEN + "&CosignerSID=" + d.ProviderSID + "&CosignerName=" + d.ProviderName,
                    async:  false,
                    success: function(msg) {
                        self.u.print(msg);
                        alert(d.ProviderName + " has been added as a cosigner to " + self.provider.name);
                    },
                    error: function(xhr, textStatus, errorThrown) {
                        //window.location = 'login.html?error=' + errorThrown;
                        self.u.print(xhr);
                        self.u.print(textStatus);
                        self.u.print(errorThrown);
                    }
                });
                self.parent.reset();
            });
        }
    }
};