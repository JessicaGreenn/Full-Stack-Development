let geoApiKey = "AIzaSyDUbADuQd-1cJGGmAxw05lwepcX8j3deUg";
let ipinfoToken = "40d143f8bdfeb2";

function clearInput()
{
    
    document.querySelector('#keyInput').value = '';
    document.querySelector('#disInput').value = 10;
    document.querySelector('#locInput').value = '';
    document.querySelector('select.category').value = 'all';
    document.querySelector('#locInput').disabled = false;
    document.querySelector('#check').checked = false;
    document.getElementById('searchHead').innerHTML = '';
    document.getElementById('searchData').innerHTML = '';
    document.getElementById('detailsCard').style.display = 'none';
    document.getElementById('nullMessage').style.display = 'none';

    
    
}
function disableLocation(){
    
    if(document.querySelector('#check').checked){
        document.querySelector('#locInput').value = '';
        document.querySelector('#locInput').disabled = "disabled";
        

    }else{
        document.querySelector('#locInput').disabled = false;
    }
    
}

const MILES_TO_METERS = 1609.344
function submitVersion(lat, lng){
    // event.preventDefault();

    
    var formElement = document.querySelector("form.container");
    var form = new FormData(formElement);
    
    if(form.get("check")=="check"){
        
        form.delete("check");
        form.append("location", document.getElementById("locInput").value)
    }
    // console.log(form.get("location"));
    // const locInput = form.get("location").split(" ");
    // var lat = locInput[0];
    // var lng = locInput[1];
    form.append("lat", lat);
    form.append("lng", lng);
    form.delete("location");

    var jsonData = {};
    form.forEach((value, key) => jsonData[key] = value);
    // var yelpData = JSON.stringify(jsonData);

    
    var backendUrl = "https://my-first-project-13580.wl.r.appspot.com/yelpParam?keyword="+ jsonData["keyword"]+ "&distance=" + jsonData["distance"] + "&category=" + jsonData["category"] + "&lat=" + jsonData["lat"]+ "&lng=" + jsonData["lng"];
    // console.log(backendUrl);

    let backendreq = new XMLHttpRequest();
    backendreq.open("GET", backendUrl, true);
    backendreq.send(jsonData);

    // receive Yelp data from the server side
    let name;
    backendreq.onreadystatechange = function(){
        console.log(backendreq);

        if (this.readyState == 4 && this.status == 200){
            var res = JSON.parse(backendreq.responseText);
            // console.log(res);
            if(res["businesses"].length == 0){
                document.getElementById("nullMessage").style.display = "block";
                document.getElementById('searchHead').innerHTML = '';
                document.getElementById('searchData').innerHTML = '';
                document.getElementById('detailsCard').style.display = 'none';
            }else{
                // window.location.hash = "#searchTable";
                document.getElementById("nullMessage").style.display = "none";
                document.getElementById('searchHead').innerHTML = '';
                document.getElementById('searchData').innerHTML = '';
                document.getElementById('detailsCard').style.display = 'none';
                var txt = '<th>No.</th><th>Image</th><th onclick = "sortSearch(2)">Business Name</th><th onclick = "sortSearch(3)">Rating</th><th onclick = "sortSearch(4)">Distance(miles)</th>'
                document.getElementById("searchHead").innerHTML = txt;
                var txtData='';
                for (let i = 0; i< res["businesses"].length; i++) {
                    txtData += "<tr><td class = 'searchId'>" + (i + 1);
                    txtData += "</td><td class = 'searchImage'>" + '<img src="' +res["businesses"][i.toString()]["image_url"] + '">';
                    txtData += "</td><td class = 'searchName' ><a class = 'storeNames' href = '#detailsCard' id = '" + res["businesses"][i.toString()]["id"] + "' onclick = 'details(this.id)' >" + res["businesses"][i.toString()]["name"] + "</a>";
                    txtData += "</td><td class = 'searchRating' >" + res["businesses"][i.toString()]["rating"];
                    // txtData += "</td><td class = 'searchDistance' >" + (res["businesses"][i.toString()]["distance"]/MILES_TO_METERS).toFixed(2);
                    txtData += "</td><td class = 'searchDistance' >" + Math.round((res["businesses"][i.toString()]["distance"] * 100 / MILES_TO_METERS)) / 100;
                    txtData += "</td></tr>";
                }
                
                document.getElementById("searchData").innerHTML = txtData;
                // name  = res["businesses"]["0"]["name"];
                // document.getElementById("keyInput").value = name;

                window.location.hash = "#searchTable";
            }
            // console.log(res);
            
            // make a table by the json data

            
        }
    }
    // backendreq.open("GET", locUrl, true);
    // backendreq.send();

    

}

function details(yelpId){
    // document.getElementById("detailsCard").style.display = "none";
    var form = new FormData();
    form.append("yelpId", yelpId);

    var jsonData = {};
    form.forEach((value, key) => jsonData[key] = value);
    // console.log(jsonData);

    let detailsreq = new XMLHttpRequest();
    detailsUrl =  "https://my-first-project-13580.wl.r.appspot.com/yelpDetails?yelpId=" + yelpId;
    detailsreq.open("GET", detailsUrl, true);
    detailsreq.send(jsonData);

    document.getElementById("detailsCard").style.display = "block";
    // receive Yelp data from the server side
    detailsreq.onreadystatechange = function(){
        console.log(detailsreq);
        // document.getElementById(yelpId).href = "#detailsCard";
            
        if (this.readyState == 4 && this.status == 200){
            var res = JSON.parse(detailsreq.responseText);
            // console.log(res);
            
            // document.getElementById("detailsCard").style.display = "block";
            
            document.getElementById("dH").innerHTML = '<br>' + res["name"];
            document.getElementById("detailsHr").style.display = "block";

            if(res["hours"] != undefined){
                document.getElementById("statusInfo").innerHTML = "Status";
                // console.log(res);
                // console.log(res["hours"]["0"]["is_open_now"])
                if(res["hours"]["0"]["is_open_now"] == true){
                    document.getElementById("open").innerHTML = "Open Now";
                    document.getElementById("open").style.backgroundColor = "#008020";
                    document.getElementById("open").style.border = "#008020 solid";

                }else{
                    document.getElementById("open").innerHTML = "Closed";
                    document.getElementById("open").style.backgroundColor = "#D72D20";
                    document.getElementById("open").style.border = "#D72D20 solid";
                }
            // }else if(res["is_closed"] != undefined){
            //     document.getElementById("statusInfo").innerHTML = "Status";
            //     if(res["is_closed"] == false){
            //         document.getElementById("open").innerHTML = "Open Now";
            //         document.getElementById("open").style.backgroundColor = "#008020";
            //         document.getElementById("open").style.border = "#008020 solid";

            //     }else{
            //         document.getElementById("open").innerHTML = "Closed";
            //         document.getElementById("open").style.backgroundColor = "#D72D20";
            //         document.getElementById("open").style.border = "#D72D20 solid";
            //     }

            }else{
                document.getElementById("statusInfo").innerHTML = '';
                document.getElementById("open").innerHTML = '';
            }

            if(res["location"]["display_address"].length > 0){
                document.getElementById("addInfo").innerHTML = "Address";
                var addr = res["location"]["display_address"]["0"];
                for(let i = 1; i < res["location"]["display_address"].length; i++){
                    addr += " " + res["location"]["display_address"][i.toString()];
                }
                document.getElementById("addr").innerHTML = addr;
            }else{
                document.getElementById("addInfo").innerHTML = '';
                document.getElementById("addr").innerHTML = '';
            }
            
            if(res["transactions"].length > 0){
                document.getElementById("transInfo").innerHTML = "Transactions Supported";
                var trans = res["transactions"]["0"];
                for(let i = 1; i < res["transactions"].length; i++){
                    trans += " | " + res["transactions"][i.toString()];
                }
                document.getElementById("trans").innerHTML = trans;
            }else{
                document.getElementById("transInfo").innerHTML = '';
                document.getElementById("trans").innerHTML = '';

            }
            
            if(res["url"] != undefined){
                document.getElementById("moreInfo").innerHTML = "More info";
                document.getElementById("yelpLink").innerHTML = "Yelp";
                document.getElementById("yelpLink").href = res["url"];
            }else{
                document.getElementById("moreInfo").innerHTML = "";
                document.getElementById("yelpLink").innerHTML = "";
            }

            // right-side data
            if(res["categories"].length > 0){
                document.getElementById("cateInfo").innerHTML = "Category";
                var cate = res["categories"]["0"]["title"];
                for(let i = 1; i < res["categories"].length; i++){
                    cate += " | " + res["categories"][i.toString()]["title"];
                }
                document.getElementById("cate").innerHTML = cate;
            }else{
                document.getElementById("cateInfo").innerHTML = '';
                document.getElementById("cate").innerHTML = '';
            }
            
            if(res["display_phone"]!=undefined){
                document.getElementById("phInfo").innerHTML = "Phone Number";
                document.getElementById("phNum").innerHTML = res["display_phone"];
            }else{
                document.getElementById("phInfo").innerHTML = "";
                document.getElementById("phNum").innerHTML = '';
            }

            if(res["price"]!=undefined){
                document.getElementById("priceInfo").innerHTML = "Price";
                document.getElementById("price").innerHTML = res["price"];
            }else{
                document.getElementById("priceInfo").innerHTML = "";
                document.getElementById("price").innerHTML = '';
            }
            
            document.getElementById("PHs").style.display = "block";
            for(let i = 0; i < 3; i++){
                if(res["photos"][i.toString()] == undefined){
                    document.getElementById("Photo"+i+1).style.backgroundImage = "none";
                    continue;
                }else{
                    var j = i+1;
                    document.getElementById("Photo"+j).style.backgroundImage = 'url("'+ res["photos"][i.toString()] + '")';
                }

            }
            // document.getElementById(yelpId).href = "#detailsCard";


            
        }

    }
    

    
            
}


// NOTE: function  sortSearch(n) uses function sortTable(n) for reference, the source website: https://www.runoob.com/w3cnote/js-sort-table.html
function sortSearch(n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("searchTable");
    switching = true;
    // set ascending order
    dir = "asc";
    //set a loop
    while (switching) {
        // loop end mark
        switching = false;
        // console.log(table.rows);
        rows = table.rows;
        

        // traverse the list from the second element(no need to change table heading)
        for (i = 1; i < (rows.length - 1); i++) {
            // judge if the elements should switch
            shouldSwitch = false;
            //one from current row and one from the next 
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            // decide if x and y should switch (asc or desc)
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    // set corresponding shouldSwitch value, end the loop
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                // set corresponding shouldSwitch value, end the loop
                shouldSwitch = true;
                break;
                }
            }
        }
        if (shouldSwitch) {
            // if shouldSwitch is true, then switch the two adjacent row
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            // switchcount increases 1 after each switch
            switchcount ++;
        } else {
            // if all elements are sort and direction is set "asc", set direction to "desc" and loop again
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }

    for(let i = 1; i< rows.length;i++){
        rows[i].getElementsByTagName("TD")[0].innerHTML = i;
    }
}   



function sm(){
    event.preventDefault();

    if(!document.getElementById("keyInput").checkValidity()){
        document.getElementById("keyInput").reportValidity();
    }else{
        if(!document.getElementById("locInput").checkValidity() && !document.querySelector('#check').checked){
            document.getElementById("locInput").reportValidity();
        }
    }
    
    //process the text
    
    // window.location.hash = "#searchTable";
    if(document.querySelector('#check').checked){
        // window.location.hash = "#searchTable";
        let ipUrl = "https://ipinfo.io/?token=" + ipinfoToken;
        // console.log(ipUrl)
        fetch(ipUrl)
        .then(res => res.json())
        .then(data => data.loc)
        .then(latlngRes => latlngRes.split(","))
        .then(latlng => {lat = latlng[0];lng = latlng[1];submitVersion(lat, lng)})
    }else{
        let locInput = document.getElementById("locInput").value;
        const locTemp1 = locInput.split(", ");
        let locTemp = locTemp1[0].split(" ");
        for(let i = 1; i < locTemp1.length; i++){
            let locTemp2 = locTemp1[i].split(" ");
            for(let j = 0; j< locTemp2.length; j++){
                locTemp.push(locTemp2[j]);
            }
        }
        let locValue = locTemp.join("+");
        let locUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + locValue + "&key=" + geoApiKey;
    
        let georeq = new XMLHttpRequest();
        var lat;
        var lng;
        georeq.onreadystatechange = function(lng, lat){
            // console.log(georeq);
            if (this.readyState == 4 && this.status == 200){
                var lnglat = JSON.parse(georeq.responseText);
                lng  = lnglat["results"]["0"]["geometry"]["location"]["lng"];
                lat  = lnglat["results"]["0"]["geometry"]["location"]["lat"];
                // console.log(lat + " " + lng)
                submitVersion(lat, lng);

            
            }
        }
        georeq.open("GET", locUrl, true);
        georeq.send();
        // return lat + " " + lng;

        // window.location.hash = "#searchTable";
    }

}



 
