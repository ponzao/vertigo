(function($) {
  $(function() {

    $.ajax({
      url: '/movies',
      success: function(result) {
                 $('#top-movies').prepend($(result));
               }
    });

    $.ajax({
      url: '/movies/1',
      success: function(result) {
                 $('#selected-movie').prepend($(result));
               }
    });

  });
})(jQuery);

