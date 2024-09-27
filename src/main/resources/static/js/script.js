console.log("this is script file")

const search=()=>{

let query=$("#search-input").val();

if(query==" "){

    $(".search-result").hide();

}
else{
   
    let url=`http://localhost:8282/search/${query}` ;

    //firstly we use url jsipe request jayegi ,now we put this url into fetch api ,fetch api is use to fetch resources or data
    //we use .then to receive promise , we have to pass a function ,response function i.e arrow function 
    //jo fetch api ke response ko receive kr ske aur usko return kre json format mein , now ye response next then mein milega
    // when we call fetch api,the response of the fetch api will comes in response function, now we"ll convert it into json format and 
    //return it,which will then received by data function and we can access the response directly using data function

    fetch(url).then((response)=>{
    return response.json();
    })
    .then((data) => {
          
        let text=`<div class='list-group'>`;

        data.forEach((contact) => {

            text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`

             $(".search-result").html(text);
             $(".search-result").show();

        });
       
    });

    }
}

const paymentStarted = () =>{
    console.log("paymentStarted");

    var amount=$("#payment_field").val();

    console.log(amount);

    if(amount=='' || amount == null){

        alert("value required !!");

        return;
    }

    // we will use  ajax to send reuest to server to create order
    $.ajax(
        {   
            // data will go to user/create_order
            url:"/user/create_order",
            // converting data into json format also adding info object with it
            data:JSON.stringify({amount:amount,info:"order_request"}),
            contentType:"application/json",
            type:"POST",
            dataType:"json",
            
            // if success is there success function will run otherwise error function will run
            // if order is created succcess, and if order created obove mentioned are details of order with particular amount entered by user
            success: function (response) {
                // this function will run when success
                console.log(response);

                if(response.status == "created" ){

                    //open payment form

                    // create an object

                    let options= {
                        key:"rzp_test_LVyNV4y0TFmjRQ",
                        amount:response.amount,
                        currency:"INR",
                        name:"contact manager",
                        description:"donation",
                        image:"http://localhost:8282/img/sachin...png",
                        order_id:response.id,

                        // if payment success hogya to kya hoga

                        handler: function (response){

                            console.log(response.razorpay_payment_id);
                            console.log(response.razorpay_order_id);
                            console.log(response.razorpay_signature);
                            console.log("payment successfull");
                            alert("congo payment successfull !!");

                        },
                        // form mein pehle se kch value dalni h 
                        "prefill": {
                            "name": "",
                            "email": "",
                            "contact": ""
                        },
                        "notes": {
                            "address": "Razorpay Corporate Office"
                        },
                        "theme": {
                            "color": "#3399cc"
                        }
                      };

                      // now we have to initiate the payment
                      let rzp = new Razorpay(options);

                      rzp.open();

                        rzp.on('payment.failed', 
                        function (response){
                         console.log(response.error.code);
                         console.log(response.error.description);
                         console.log(response.error.source);
                         console.log(response.error.step);
                         console.log(response.error.reason);
                         console.log(response.error.metadata.order_id);
                         console.log(response.error.metadata.payment_id);
                         alert("opps , Payment failed !!")
                    });
                }

            },
            error: function (error) {

                console.log(error);
                alert("something went wrong !!");
            },
            

        });
};
