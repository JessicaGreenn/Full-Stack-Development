var express = require('express');
var app = express();
let geoApiKey = "AIzaSyDUbADuQd-1cJGGmAxw05lwepcX8j3deUg";
let ipinfoToken = "40d143f8bdfeb2";
let testlat = 34.047056;
let testlng = -118.256544;
let testid = "TkFEKhsCixPWlShULKvMdQ";
const MILES_TO_METERS = 1609;
const cors = require('cors');

app.use(cors({
   origin: '*'
}));
// app.use(express.static('HW8/src'));
app.use(express.static(process.cwd()+"/dist/my-app"));


app.get('/search', function (req, res) {
   // res.send('Hello World');
   res.sendFile(process.cwd()+"/dist/my-app/index.html")
  
})
app.get('/bookings', function (req, res) {
   // res.send('Hello World');
   res.sendFile(process.cwd()+"/dist/my-app/index.html")
  
})

// This calls Yelp autoComplete API on the /autoComplete page.
app.get('/autoComplete', function (req, res) {
   // console.log(req.query);
   const axios = require('axios');
   var text = req.query.text;
   autoUrl = 'https://api.yelp.com/v3/autocomplete?text=' + text;
   axios.get(autoUrl, {
      headers: {
         'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'
      }
   }).then(function (response){
      // console.log(response.data);
      var business = [];
      var m = response.data.categories.length;
      var n = response.data.terms.length;
      for(let i = 0; i < m + n; i++){
         if(i < m){
            business[i] = response.data.categories[i].title;
         }else{
            business[i] = response.data.terms[i - m].text;
         }
      }
      res.send(business);
   }).catch(function (error) {
         // handle error
      console.log(error);
   });
})

// This calls Yelp businesses search API on the /yelpParam page.
app.get('/yelpParam', function (req, res) {
   // console.log(req);
   let termList = req.query.term.split(' ');
   var term;
   if(termList.length == 1){
      term = termList[0];
   }else{
      term = termList.join('+');
   }
   let locInfo = req.query.locInfo;
   
   let radiusStr = req.query.radius;
   let categories = req.query.categories;

   let radius = parseInt(radiusStr) * MILES_TO_METERS;
   // console.log(radius);

   let geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + locInfo + "&key=" + geoApiKey;
   const axios = require('axios');

   // Make a request for geoCoding with address and key
   axios.get(geoUrl)
      .then(function (response) {
         // handle success
         var geoInfo = {};
         geoInfo["lat"] = response.data.results[0].geometry.location.lat;
         geoInfo["lng"] = response.data.results[0].geometry.location.lng;

         // console.log(geoInfo);

         return geoInfo;

      }).then(function (geoInfo) {
         // handle success
         // console.log(geoInfo);
         
         let yelpUrl = "https://api.yelp.com/v3/businesses/search?term="+term + "&latitude=" + geoInfo["lat"] + "&longitude=" + geoInfo["lng"] + "&categories=" + categories + "&radius=" + radius;
         // console.log(yelpUrl);

         const yelpAxios = require('axios');

         yelpAxios.get(yelpUrl, {
            headers: {
               'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'
            }
         }).then(function (response){
            // console.log("yelp call succeeded!");

            const MILES_TO_METERS = 1609.344;
            var n = Math.min(response.data.businesses.length, 10);
            var results = [];
            for(let i = 0; i < n; i++){
      
               results[i] = response.data.businesses[i];
               results[i].idNum = i + 1;
               results[i].distance = Math.round(parseFloat(response.data.businesses[i].distance) * 100 / MILES_TO_METERS) / 100;
            }
            console.log(results);
            res.send(results);
            
         });
      }).catch(function (error) {
         // handle error
         console.log(error);
      });


});

// call Yelp business when autolocating
app.get('/autoYelpParam', function (req, res) {
   // console.log(req);
   let termList = req.query.term.split(' ');
   var term;
   if(termList.length == 1){
      term = termList[0];
   }else{
      term = termList.join('+');
   }
   let lat = req.query.lat;
   let lng = req.query.lng;
   let radiusStr = req.query.radius;
   let categories = req.query.categories;

   let radius = parseInt(radiusStr) * MILES_TO_METERS;
   // console.log(radius);

   let yelpUrl = "https://api.yelp.com/v3/businesses/search?term=" + term + "&latitude=" + lat + "&longitude=" + lng + "&categories=" + categories + "&radius=" + radius;
   // console.log(yelpUrl);

   const yelpAxios = require('axios');

   yelpAxios.get(yelpUrl, {
      headers: {
         'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'
      }
   }).then(function (response){
      // console.log(response.data.businesses);
      var n = Math.min(response.data.businesses.length, 10);
      var results = [];
      for(let i = 0; i < n; i++){

         results[i] = response.data.businesses[i];
         results[i].idNum = i + 1;
         results[i].distance = Math.round(parseFloat(response.data.businesses[i].distance) * 100 / MILES_TO_METERS) / 100;
      }
      console.log(results);
      res.send(results);
      
      
   }).catch(function (error) {
      // handle error
      console.log(error);
   });


});

// This calls Yelp details API on the /yelpDetails page.
app.get('/yelpDetails', function (req, res) {
   var id = req.query.id;
   // console.log(id);
   const axios = require('axios');
   detailUrl = 'https://api.yelp.com/v3/businesses/' + id;
   axios.get(detailUrl, {
      headers: {
         'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'
      }
   }).then(function (response){
      var details = {};
      details.name = response.data.name;
      details.phone = response.data.phone;
      details.coordinates = response.data.coordinates;
      details.id = response.data.id;
      details.price = response.data.price;
      details.yelpLink = response.data.url;
      details.coordinates = response.data.coordinates;
      details.status = response.data.hours[0].is_open_now;
      if(response.data.categories.length > 0){
         var category = response.data.categories[0].title;
         for(let i = 1; i < response.data.categories.length; i++){
            category += ' | ' + response.data.categories[i].title;
         }
         details.category = category;
      }
      details.location = response.data.location;
      details.address = response.data.location.address1 + response.data.location.address2 + response.data.location.address3;
      details.photos = response.data.photos;
      

      console.log(details);
      res.send(details);

   }).catch(function (error) {
      // handle error
      console.log(error);
   });
})

// This calls Yelp review API on the /yelpReview page.
app.get('/yelpReview', function (req, res) {
   var id = req.query.id;
   const axios = require('axios');
   // console.log(testid);
   reviewUrl = `https://api.yelp.com/v3/businesses/${id}/reviews`;
   axios.get(reviewUrl, {
      headers: {
         'Authorization': 'Bearer 6ykbycSckHcr_i0yGNee-AhL4TrTWIjQ1B8Qlazi4JXkUv8OwsTHC3w7WxItejyxx_Q_jrMrslmpcuFAxCBGsrTk00VPwv512n39F5uQdMwGTtXUlWyXf9SP5T8-Y3Yx'
      }
   }).then(function (response){
      // console.log(response.data);
      var n = response.data.reviews.length;
      var reviewsRcvd = response.data.reviews;
      // console.log(Math.min(3,response.data.reviews.length));
      var revs = [];
      
      for(let i = 0; i < Math.min(3, n); i++){
         var revsElmt = {};
         revsElmt.rating = reviewsRcvd[i].rating;
         revsElmt.text = reviewsRcvd[i].text;
         revsElmt.time = reviewsRcvd[i].time_created;
         revsElmt.name = reviewsRcvd[i].user.name;
         revs[i] = revsElmt;
      }
      res.send(revs);
      
   }).catch(function (error) {
         // handle error
      console.log(error);
   });
})



const PORT = process.env.port || 8080;
var server = app.listen(PORT, function () {
   var host = server.address().address
   var port = server.address().port
   
   console.log("Example app listening at http://%s:%s", host, port)
})