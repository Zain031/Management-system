import { useEffect, useState } from "react";

const MidtransSnap = ({ transactionToken }) => {
  const [isSnapLoaded, setIsSnapLoaded] = useState(false);

  useEffect(() => {
    const checkSnap = setInterval(() => {
      if (window.snap) {
        setIsSnapLoaded(true);
        clearInterval(checkSnap);
      }
    }, 500);

    return () => clearInterval(checkSnap);
  }, []);

  const handlePay = () => {
    if (!isSnapLoaded) {
      alert("Midtrans Snap is not loaded yet.");
      return;
    }

    window.snap.embed("71c14c6e-cf5c-490c-ac47-599daaaf7eb5", {
      embedId: "snap-container",
      onSuccess: function (result) {
        alert("Payment success!");
        console.log(result);
      },
      onPending: function (result) {
        alert("Waiting for your payment!");
        console.log(result);
      },
      onError: function (result) {
        alert("Payment failed!");
        console.log(result);
      },
      onClose: function () {
        alert("You closed the popup without finishing the payment.");
      },
    });
  };

  return (
    <div>
      <button id="pay-button" onClick={handlePay} disabled={!isSnapLoaded}>
        {isSnapLoaded ? "Pay!" : "Loading..."}
      </button>

      {/* You can add the desired ID as a reference for the embedId parameter. */}
      <div id="snap-container"></div>
    </div>
  );
};

export default MidtransSnap;
