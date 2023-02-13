import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MatAutocompleteModule } from '@angular/material/autocomplete';


import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';
import { LastValueFromConfig } from 'rxjs/internal/lastValueFrom';

//for validation
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
// import { CustomvalidationService } from '../services/customvalidation.service';



const PATH = "https://my-second-project-33725.wl.r.appspot.com/autoComplete?keyword=";
const API_KEY = "e8067b53";
const geoApiKey = "AIzaSyDUbADuQd-1cJGGmAxw05lwepcX8j3deUg";
const ipinfoToken = "40d143f8bdfeb2";
const BACKEND_HEAD = "https://my-second-project-33725.wl.r.appspot.com";
const MILES_TO_METERS = 1609.344;

//for validation
// @Injectable({
//   providedIn: 'root'
// })

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SearchComponent implements OnInit {
  // searchKey:string = '';
  // searchDis:number = 10;
  // searchCate:string = 'all';
  registerForm!: FormGroup;
  rsvpSubmitted = false;
  



  searchLoc = '';
  
  autoCheck:boolean =  false;
  hasResults:boolean = false;
  noResults:boolean = false;
  results:any;
  hasDetails:boolean = false;
  details:any;
  reviews:any;
  mapOptions: google.maps.MapOptions = {
    center : { lat: 34.047056, lng: -118.256544 },
    zoom : 14
  };
  marker = {
    position: { lat: 34.047056, lng: -118.256544 }
  };
  
  // distance:any;
  searchBusiCtrl = new FormControl();
  minLengthTerm = 1;
  errorMsg!: string;
  filteredBusinesses: any;
  isLoading = false;
  selectedBusi: any = "";
  local:any = {};
  emailReq:boolean = false;
  emailValid: boolean = false;
  dateReq:boolean = false;
  // hReq:boolean = false;
  // minReq:boolean = false;
  
  
  
  //today's date
  // todayDate:Date = new Date();
  // todayString: string = this.todayDate.toLocaleDateString();
  maxDate:any;


  //any date
  // someDate: Date = new Date(anydate);
  

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
  
  ) { }

  onSelected() {
    // console.log(this.selectedBusi);
    (<HTMLInputElement>document.getElementById("keyInput")).value = this.selectedBusi;
  }

  displayWith(value: any) {
    return value?.Title;
  }

  // clearSelection() {
  //   this.selectedMovie = "";
  //   this.filteredMovies = [];
  // }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      rsvpdate: ['', Validators.required],
      // rsvpmin: ['', Validators.required],
      // rsvpH: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });
    


    var dtToday = new Date();
    var month = dtToday.getMonth() + 1;
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();
    if(month < 10){
      var monthStr = '0' + month.toString();
    }else{
      var monthStr = month.toString();
    }
        
    if(day < 10){
      var dayStr = '0' + day.toString();
    }else{
      var dayStr = day.toString();
    }
    this.maxDate = year + '-' + monthStr + '-' + dayStr;

    this.searchBusiCtrl.valueChanges
    .pipe(
      filter(res => {
        return res !== null && res.length >= this.minLengthTerm
      }),
      distinctUntilChanged(),
      debounceTime(1000),
      tap(() => {
        this.errorMsg = "";
        this.filteredBusinesses = [];
        this.isLoading = true;
      }),
      switchMap(value => this.http.get(BACKEND_HEAD + '/autoComplete?text=' + value)
        .pipe(
          finalize(() => {
            this.isLoading = false
          }),
        )
      )
    )
    .subscribe((data: any) => {
      if (data == undefined) {
        this.filteredBusinesses = [];
      } else {
        // console.log(data);
        this.errorMsg = "";
        this.filteredBusinesses = data;
      }
      // console.log(this.filteredBusinesses);
    });

    if(localStorage.length == 0){
      this.local = {};
    }else{
      for (var i = 0; i < localStorage.length; i++){
        var myKey = localStorage.key(i);

        if(myKey != null){
          var val = localStorage.getItem(myKey);
          
  
          if(val != null && myKey != undefined){

            this.local[myKey] = val;
  
          }
        }
        
      }
    }
    
    console.log(this.local);
  }

  get registerFormControl() {
    return this.registerForm.controls;
  }

  clearInput()
  {
    // this.searchKey = '';
    // this.searchDis = 10;
    // this.searchCate = 'all';
    // this.searchLoc = '';
    // this.autoCheck = false;
    this.autoCheck = false;
    this.hasResults = false;
    this.noResults = false;
    this.hasDetails = false;
    
    console.log(localStorage);
    console.log(this.local);
    
  }
  disableLoc(){
    (<HTMLInputElement>document.getElementById("locInput")).value = '';
    this.autoCheck = !this.autoCheck;
    
  }
  submitData(){

    if(!(<HTMLInputElement>document.getElementById("keyInput")).checkValidity()){
      (<HTMLInputElement>document.getElementById("keyInput")).reportValidity();
    }else if(!(<HTMLInputElement>document.getElementById("disInput")).checkValidity()){
      (<HTMLInputElement>document.getElementById("disInput")).reportValidity();
    }else{
      if(!(<HTMLInputElement>document.getElementById("locInput")).checkValidity() && !(<HTMLInputElement>document.querySelector('#autoDetect')).checked){
        (<HTMLInputElement>document.getElementById("locInput")).reportValidity();
      }
    }

    if(this.autoCheck){
      const ipUrl = "https://ipinfo.io/?token=" + ipinfoToken;
    
      this.http.get(ipUrl)
      .subscribe((data: any) => {
        if (data.loc == undefined) {
          
          this.searchLoc = '';
        } else {
          var latlng = data.loc.split(',');
          var lat = latlng[0];
          var lng = latlng[1];
          this.autoSearch(lat, lng);
        }
        
      });
    }else{
      var locInput = (<HTMLInputElement>document.getElementById("locInput")).value;
      var locTemp1 = locInput.split(" ");
      var locInfo = locTemp1.join("+");
      this.inputSearch(locInfo);
    }

    
  }
  autoSearch(lat:string, lng:string){
    // console.log(this.searchLoc);
    // var term = (<HTMLInputElement>document.getElementById("keyInput")).value;
    let termList = (<HTMLInputElement>document.getElementById("keyInput")).value.split(" ");
    var term;
    if(termList.length == 1){
       term = termList[0];
    }else{
       term = termList.join('+');
    }
    var radius = (<HTMLInputElement>document.getElementById("disInput")).value;
    var categories = (<HTMLInputElement>document.getElementById("cateInput")).value;


    const backendUrl = BACKEND_HEAD + "/autoYelpParam?term=" + term + "&lat=" + lat + "&lng=" + lng + "&radius=" + radius + "&categories=" + categories;
    // console.log(backendUrl);
    const headers = new HttpHeaders({'Access-Control-Allow-Origin' : ' * '});
    this.http.get(backendUrl, {headers})
      .subscribe((data: any) => {
        if (data == undefined) {
          
          console.log("no responses");
        } else {
          // console.log(data);

          if(data.length == 0){
            this.noResults = true;
            this.hasResults = false;
            this.hasDetails = false;
          }else{
            this.results = data;
            // this.results.distance = Math.round(this.results.distance * 100 / MILES_TO_METERS) / 100;
            this.hasResults = true;
            this.noResults = false;
            this.hasDetails = false;
            
          }

        }
        
      });
  }

  inputSearch(locInfo:string){

    let termList = (<HTMLInputElement>document.getElementById("keyInput")).value.split(" ");
    var term;
    if(termList.length == 1){
       term = termList[0];
    }else{
       term = termList.join('+');
    }
    var radius = (<HTMLInputElement>document.getElementById("disInput")).value;
    var categories = (<HTMLInputElement>document.getElementById("cateInput")).value;
    // console.log(locInfo);
    const backendUrl = BACKEND_HEAD + "/yelpParam?term=" + term + "&locInfo=" + locInfo + "&radius=" + radius + "&categories=" + categories;
    // console.log(backendUrl);
    const headers = new HttpHeaders({'Access-Control-Allow-Origin' : ' * '});
    this.http.get(backendUrl, {headers})
      .subscribe((data: any) => {
        if (data == undefined) {
          
          console.log("no responses");
        } else {
          // console.log(data);

          if(data.length == 0){
            this.noResults = true;
            this.hasResults = false;
            this.hasDetails = false;
          }else{
            this.results = data;
            // this.results.distance = Math.round(this.results.distance * 100 / MILES_TO_METERS) / 100;
            this.hasResults = true;
            this.noResults = false;
            this.hasDetails = false;
            
          }

        }
        
      });
  }

  displayDetails(id:string){
    // console.log(id);
    const backendUrl = BACKEND_HEAD + "/yelpDetails?id=" + id;
    // console.log(backendUrl);
    const headers = new HttpHeaders({'Access-Control-Allow-Origin' : ' * '});
    
    this.http.get(backendUrl, {headers})
      .subscribe((data: any) => {
        if (data == undefined) {
          console.log("no responses");
          

        } else {
          // console.log(data);
          this.details = data;
          
          var latVal = parseFloat(data.coordinates.latitude);
          var lngVal = parseFloat(data.coordinates.longitude);
          // console.log(latVal + ' ' + lngVal);
          this.marker.position.lat = latVal;
          this.marker.position.lng = lngVal;
          this.mapOptions.center = this.marker.position;

          // console.log(this.marker);
          // console.log(this.mapOptions);
          this.hasResults = false;
          this.noResults = false;
          this.hasDetails = true;

        }
        
      });
    
    const reviewUrl = BACKEND_HEAD + "/yelpReview?id=" + id;
    // console.log(reviewUrl);
    this.http.get(reviewUrl, {headers})
      .subscribe((data: any) => {
        if (data == undefined) {
          console.log("no responses");

        } else {
          // console.log(data);
          this.reviews = data;

        }
        
      });

  }

  back(){
    this.hasResults = true;
    this.noResults = false;
    this.hasDetails = false;
  }
  shareFB(){
    const fbUrl = 'https://www.facebook.com/sharer/sharer.php?u=' + this.details.yelpLink;
    // window.open(fbUrl , '_blank');
    window.open(fbUrl);
  }
  shareTW(){
    const twUrl = 'https://twitter.com/intent/tweet?text=Check ' + this.details.name + ' on Yelp. ' + '\n' + this.details.yelpLink;
    // window.open(twUrl, '_blank');
    window.open(twUrl);
  }
  // smt(){
  //   if(!(<HTMLInputElement>document.getElementById("validationCustomUsername")).checkValidity()){
  //     (<HTMLInputElement>document.getElementById("validationCustomUsername")).reportValidity();
  //   }else if(!(<HTMLInputElement>document.getElementById("validationCustom03")).checkValidity()){
  //     (<HTMLInputElement>document.getElementById("validationCustom03")).reportValidity();
  //   }else if(!(<HTMLInputElement>document.getElementById("validationCustom04")).checkValidity()){
  //     (<HTMLInputElement>document.getElementById("validationCustom04")).reportValidity();
  //   }else if(!(<HTMLInputElement>document.getElementById("validationCustom05")).checkValidity()){
  //     (<HTMLInputElement>document.getElementById("validationCustom05")).reportValidity();
  //   }
  // }
  submitRsvp(){
    this.rsvpSubmitted = true;

    if((this.registerFormControl['email'].errors != null) && (this.registerFormControl['rsvpdate'].errors != null)){
      this.emailReq = (this.registerFormControl['email'].touched || this.rsvpSubmitted) && this.registerFormControl['email'].errors['required'];
      this.emailValid = this.registerFormControl['email'].touched && this.registerFormControl['email'].errors['email'];
      this.dateReq = (this.registerFormControl['rsvpdate'].touched || this.rsvpSubmitted) && this.registerFormControl['rsvpdate'].errors['required'];

    }
    // if(this.registerFormControl['rsvpH'].errors != null){
    //   this.hReq = (this.registerFormControl['rsvpH'].touched || this.rsvpSubmitted) && this.registerFormControl['rsvpH'].errors['required'];
    // }
    // if(this.registerFormControl['rsvpmin'].errors != null){
    //   this.minReq = (this.registerFormControl['rsvpmin'].touched || this.rsvpSubmitted) && this.registerFormControl['rsvpmin'].errors['required'];
    // }
    
    if (this.registerForm.valid) {
      // console.log(localStorage);
      var key = this.details.id;
      var temp : any = {};
    
      temp.name = this.details.name;
      temp.email = (<HTMLInputElement>document.getElementById("validationCustomUsername")).value;
      temp.date = (<HTMLInputElement>document.getElementById("validationCustom03")).value;
      var hour = (<HTMLInputElement>document.getElementById("validationCustom04")).value;
      var minute = (<HTMLInputElement>document.getElementById("validationCustom05")).value;
      var time = hour + ':' + minute;
      temp.time = time;
      // console.log(temp);

      var val = <JSON>temp;
      localStorage.setItem(key, JSON.stringify(val));
      console.log(localStorage);
    
      window.alert("Reservation created!");
    
      this.local[this.details.id] = localStorage.getItem(key);
      (<HTMLInputElement>document.getElementById("closeRsvp")).click();
      // console.log(this.local);
    }
    
    
  }

  cancelRsvp(){
    console.log(this.details.id);
    localStorage.removeItem(this.details.id);
    
    console.log(localStorage);

    window.alert("Reservation cancelled!");
    delete this.local[this.details.id];
    // console.log(this.local);

  }

}
