<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <script src="https://cdn.bootcss.com/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
  </head>
  <body>
    <div id="myQrcode"></div>
    <div id="orderId">${orderId}</div>
    <div id="returnUrl">${returnUrl}</div>
  </body>

  <script>
    jQuery("#myQrcode").qrcode({
      text: "${codeUrl}",
    });
    
    $(function(){
    	setInterval(function(){
    		$.ajax({
    			url: "/pay/queryByOrderId",
    			data: {
    				orderId: $('#orderId').text(),
    			},
    			success: function(result) {
    				console.log("result: ", result)
    				if (result.platformStatus != null && result.platformStatus === 'SUCCESS') {
    					location.href = $('#returnUrl').text()
    				}
    			},
    			error: function(err) {
    				console.log("err: ", err)
    			}
    		})
    	}, 2000)
    })
  </script>
</html>
