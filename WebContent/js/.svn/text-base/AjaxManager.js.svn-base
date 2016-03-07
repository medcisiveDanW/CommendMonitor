var AjaxManager = function() {
    var self = this;
    (function() {
        // init stuff goes here
    })(jQuery);

    self.deleteFeedback = function(duz,timestamp) {
        $.ajax({
            type:   "post",
            url:    "updateFeedback.jsp",
            data:   'DUZ=' + duz + '&TIMESTAMP=' + timestamp + '&DELETE=true',
            async: true,
            success: function(msg) {
                msg = msg.replace(/^\s+|\s+$/g, '') ;
            }
        });
    }
    self.updateFeedback = function(duz,timestamp,action) {
        $.ajax({
            type:   "post",
            url:    "updateFeedback.jsp",
            data:   'DUZ=' + duz + '&TIMESTAMP=' + timestamp + '&ACTION=' + action + '&DELETE=false',
            async: true,
            success: function(msg) {
                msg = msg.replace(/^\s+|\s+$/g, '') ;
            }
        });
    }
    return self;
};