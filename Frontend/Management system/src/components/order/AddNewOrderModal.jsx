import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import ButtonAdd from "../buttons/ButtonAdd";
import { Trash2 } from "lucide-react";
import { useDispatch } from "react-redux";
import {
  changeQuantityInCart,
  removeProductFromCart,
} from "../../redux/feature/orderSlice";
import { useEffect } from "react";

const AddNewOrderModal = ({
  handleAddNewOrder,
  setCustomerNameForCreatingOrder,
  customerNameForCreatingOrder,
  products,
  idProductForCreatingOrder,
  setIdProductForCreatingOrder,
  quantityForCreatingOrder,
  setQuantityForCreatingOrder,
  addProductForOrder,
  cart,
  isSuccessAddOrder,
  redirectURL,
  onButtonCloseAddNewOrder,
  isCustomerNameValid = true,
  isOrderDetailsValid = true,
  payWith,
  setPayWith,
  amount,
  setAmount,
  isAmountValid = true,
}) => {
  const dispatch = useDispatch();

  const removeQuantity = (id, quantity) => {
    dispatch(removeProductFromCart({ id, quantity }));
  };

  const extractNumber = (value) => {
    const sanitizedValue = value.replace(/[^0-9]/g, "");
    return parseInt(sanitizedValue, 10) || 0;
  };

  const validateAmount = (amount) => {
    const numericAmount = extractNumber(amount);
    setAmount(numericAmount);
  };

  useEffect(() => {
    if (!isSuccessAddOrder || !redirectURL || payWith !== "QRIS") return;
    window.open(redirectURL, "_blank");
  }, [redirectURL, isSuccessAddOrder, payWith]);

  return (
    <dialog id="add_new_order_modal" className="modal">
      <div className="modal-box">
        <form onSubmit={handleAddNewOrder}>
          <select
            name="pay_with"
            value={payWith}
            disabled={isSuccessAddOrder}
            onChange={(e) => setPayWith(e.target.value)}
            className="select select-bordered w-full">
            <option selected value="">
              Pay With
            </option>
            <option value="QRIS">QRIS</option>
            <option value="CASH">Cash</option>
          </select>
          {!payWith.trim() == "" && (
            <>
              <input
                type="name"
                name="customerName"
                className="input input-bordered input-ghost w-full my-2"
                placeholder="Customer Name"
                disabled={isSuccessAddOrder}
                value={customerNameForCreatingOrder}
                onChange={(e) =>
                  setCustomerNameForCreatingOrder(e.target.value)
                }
              />
              {!isCustomerNameValid && (
                <p className="text-red-500">Customer Name is required</p>
              )}
              {!isSuccessAddOrder && (
                <section className="flex gap-2 justify-center items-center">
                  <select
                    name="customer_name"
                    value={idProductForCreatingOrder}
                    onChange={(e) =>
                      setIdProductForCreatingOrder(e.target.value)
                    }
                    className="select select-bordered w-full">
                    <option selected value="">
                      Select Product Name
                    </option>
                    {products.map((product) => (
                      <option
                        key={product.id_product}
                        value={product.id_product}>
                        {product.product_name}
                      </option>
                    ))}
                  </select>
                  <input
                    type="number"
                    name="quantity"
                    value={quantityForCreatingOrder}
                    onChange={(e) =>
                      setQuantityForCreatingOrder(e.target.value)
                    }
                    className="input input-bordered input-ghost w-full my-2"
                    placeholder="Quantity"
                  />
                  <ButtonAdd onPress={addProductForOrder} dataTip={"Product"} />
                </section>
              )}
              {payWith === "CASH" && (
                <>
                  <label
                    className="text-gray-800 font-semibold block mt-3 mb-1 text-md"
                    htmlFor="price">
                    Amount
                  </label>
                  <input
                    type="text"
                    name="price"
                    min={cart?.totalPrice}
                    disabled={isSuccessAddOrder}
                    onChange={(e) => validateAmount(e.target.value)}
                    value={amount}
                    className="input input-bordered input-ghost w-full my-2"
                    placeholder="Price"
                  />
                  {!isAmountValid && (
                    <p className="text-red-500">Amount is more than total</p>
                  )}
                </>
              )}
            </>
          )}
          <section>
            <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md mt-2 mb-4">
              <table className="table">
                <thead>
                  <tr>
                    <th></th>
                    <th>Product Name</th>
                    <th>Quantity</th>
                  </tr>
                </thead>
                <tbody>
                  {Array.isArray(cart?.orderDetails) &&
                  cart?.orderDetails.length > 0 ? (
                    <>
                      {cart?.orderDetails.map((item, index) => (
                        <tr key={item.id_order_details}>
                          <th>{++index}</th>
                          <td>{item.product_name}</td>
                          <td className="flex items-center">
                            <input
                              value={item.quantity}
                              disabled={isSuccessAddOrder}
                              onChange={(e) => {
                                dispatch(
                                  changeQuantityInCart({
                                    id: item.id_product,
                                    quantity: e.target.value,
                                  })
                                );
                              }}
                              className="w-1/2 rounded-md border-1 py-1 px-2"
                            />
                            {!isSuccessAddOrder && (
                              <button
                                type="button"
                                className="tooltip"
                                data-tip="Remove Product">
                                <Trash2
                                  size={24}
                                  color="red"
                                  onClick={() =>
                                    removeQuantity(
                                      item?.id_product,
                                      item?.quantity
                                    )
                                  }
                                />
                              </button>
                            )}
                          </td>
                        </tr>
                      ))}
                      <tr>
                        <td>Customer Name</td>
                        <td colSpan="2">{cart?.customerName}</td>
                      </tr>
                      {payWith === "CASH" && (
                        <tr>
                          <td>Amount</td>
                          <td colSpan="2">{numberToIDR(amount)}</td>
                        </tr>
                      )}
                      <tr>
                        <td>Total</td>
                        <td colSpan="2">{numberToIDR(cart?.totalPrice)}</td>
                      </tr>
                      {payWith === "CASH" && (
                        <tr>
                          <td>Change</td>
                          <td>{numberToIDR(amount - cart?.totalPrice)}</td>
                        </tr>
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="text-center">
                        No Materials available
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
              {!isOrderDetailsValid && (
                <p className="text-red-500">Order Details is greater than 0</p>
              )}
            </div>
          </section>
          {!isSuccessAddOrder && (
            <button
              className="btn btn-outline btn-primary w-full"
              type="submit">
              PAY
            </button>
          )}
        </form>
        <div className="modal-action">
          <form method="dialog">
            <button className="btn" onClick={onButtonCloseAddNewOrder}>
              Close
            </button>
          </form>
        </div>
      </div>
    </dialog>
  );
};

export default AddNewOrderModal;
