angular.module( 'symbiosart', ['ngResource'] )
    .factory( 'RestService', RestService )
    .controller( 'MainCtrl', MainCtrl );


function RestService( $resource ){
    return $resource( '', {}, {
        suggestions: {method: 'POST', url: '/rest/images/suggestions/:nbr', params: {nbr: 4}, isArray: true},
        details    : {method: 'GET', url: '/rest/images/:id', params: {_id: '@_id'}}
    } );
}

function MainCtrl( RestService, $interval, $q ){
    var self = this;

    self.gridSize = 12;
    self.images = [];
    self.displayedImages = [];
    self.tagsVector = {};
    self.seenIds = [];

    self.like = like;
    self.dislike = dislike;

    init();


    $interval( function(){

        var index = getRandomInt( 0, self.displayedImages.length );
        self.displayedImages[index] = self.images.pop();

        //fetch().then( function(){
        //    var index = getRandomInt( 0, self.displayedImages.length );
        //    self.displayedImages[index] = self.images.pop();
        //} );
    }, 10000 );


    function init(){
        fetch().then( function(){
            for( var i = 0; i < self.gridSize; i++ ){
                self.displayedImages.push( self.images.pop() );
            }
            fetch();
        } );

    }


    function fetch(){
        var deferred = $q.defer();
        //var nbr = 5;
        //if(self.displayedImages.length < self.gridSize) nbr = self.gridSize * 2;
        var nbr = 20;

        RestService.suggestions( {nbr: nbr}, self.tagsVector, function( result ){
            var n = 0;
            result.forEach( function( val ){
                if( !self.seenIds[val._id] ){
                    self.seenIds[val._id] = true;
                    self.images.push( val );
                    n++;
                }
            }, function(error){
                console.log(error);
            } );

            console.log("added " + n + " images (" + self.images.length + ")");

            deferred.resolve();
        } );

        return deferred.promise;
    }

    function likedislike( img, weight ){
        img.tags.forEach( function( key ){
            if( self.tagsVector.hasOwnProperty( key ) ){
                self.tagsVector[key] += weight;
            }else{
                self.tagsVector[key] = weight;
            }
        } );

        var index = self.displayedImages.indexOf( img );
        self.displayedImages[index] = {};

        fetch().then(function(){
            self.displayedImages[index] = self.images.pop();
        });

    }

    function dislike( img ){
        likedislike( img, -1 );
    }

    function like( img ){
        likedislike( img, 1 );
    }

    function getRandomInt( min, max ){
        return Math.floor( Math.random() * (max - min + 1) ) + min;
    }

}
