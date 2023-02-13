import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.css']
})
export class BookingsComponent implements OnInit {
  hasBookings: boolean = false;
  localBookings: any = [];

  constructor() { }

  ngOnInit(): void {
    if(localStorage.length == 0){
      this.hasBookings = false;
    }else{
      this.hasBookings = true;
      for (var i = 0; i < localStorage.length; i++){
        var temp = localStorage.key(i);
        this.localBookings[i] = {};
        
        console.log(temp);
        // var temp = '';
        // if(temp1!=undefined){
        //   temp = temp1[1];
        // }
        
        if(temp != null){
          var val = localStorage.getItem(temp);
          console.log(val);

          if(val != null){
            this.localBookings[i] = JSON.parse(val);
            this.localBookings[i].id = i + 1;
            this.localBookings[i].code = temp;

          }
        }
        
      }
      console.log(this.localBookings);
      
    }
  }

  cancelReservation(code: string){
    for (let i = 0; i < this.localBookings.length; i++){
      if(this.localBookings[i].code == code){
        var localKey = this.localBookings[i].code;
        console.log(this.localBookings[i]);
        console.log(this.localBookings[i].code);
        localStorage.removeItem(localKey);
        delete this.localBookings[i];
      }
    }
    
    console.log(localStorage);
    window.alert("Reservation cancelled!");
    if(localStorage.length == 0){
      this.hasBookings = false;
    }else{
      this.localBookings = [];
      for (var i = 0; i < localStorage.length; i++){
        var temp = localStorage.key(i);
        this.localBookings[i] = {};
        
        // console.log(temp);
        // var temp = '';
        // if(temp1!=undefined){
        //   temp = temp1[1];
        // }
        
        if(temp != null){
          var val = localStorage.getItem(temp);
          // console.log(val);

          if(val != null){
            this.localBookings[i] = JSON.parse(val);
            this.localBookings[i].id = i + 1;
            this.localBookings[i].code = temp;

          }
        }
        
      }
    }
    
    console.log(this.localBookings);
  }

}
