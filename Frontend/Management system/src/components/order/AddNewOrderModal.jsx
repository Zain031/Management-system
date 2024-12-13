import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import ButtonAdd from "../buttons/ButtonAdd";
import { Trash2 } from "lucide-react";
import { useDispatch } from "react-redux";
import {
  addProductToCart,
  changeQuantityInCart,
  removeProductFromCart,
} from "../../redux/feature/orderSlice";

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
}) => {
  const dispatch = useDispatch();
  const addQuantity = (id, quantity) => {
    dispatch(addProductToCart({ id, quantity }));
  };

  const removeQuantity = (id, quantity) => {
    dispatch(removeProductFromCart({ id, quantity }));
  };

  return (
    <dialog id="add_new_order_modal" className="modal">
      <div className="modal-box">
        <form onSubmit={handleAddNewOrder}>
          <input
            type="name"
            name="customerName"
            className="input input-bordered input-ghost w-full my-2"
            placeholder="Customer Name"
            value={customerNameForCreatingOrder}
            onChange={(e) => setCustomerNameForCreatingOrder(e.target.value)}
          />
          {!isSuccessAddOrder && (
            <section className="flex gap-2 justify-center items-center">
              <select
                name="customer_name"
                value={idProductForCreatingOrder}
                onChange={(e) => setIdProductForCreatingOrder(e.target.value)}
                className="select select-bordered w-full">
                <option selected value="">
                  Select Product Name
                </option>
                {products.map((product) => (
                  <option key={product.id_product} value={product.id_product}>
                    {product.product_name}
                  </option>
                ))}
              </select>
              <input
                type="number"
                name="quantity"
                value={quantityForCreatingOrder}
                onChange={(e) => setQuantityForCreatingOrder(e.target.value)}
                className="input input-bordered input-ghost w-full my-2"
                placeholder="Quantity"
              />
              <ButtonAdd onPress={addProductForOrder} dataTip={"Product"} />
            </section>
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
                              onChange={(e) => {
                                console.log("Id pro", item.id_product);
                                dispatch(
                                  changeQuantityInCart({
                                    id: item.id_product,
                                    quantity: e.target.value,
                                  })
                                );
                              }}
                              className="w-1/2 rounded-md border-1 py-1 px-2"
                            />
                            <button
                              type="button"
                              className="tooltip"
                              data-tip="Remove Product">
                              <Trash2
                                size={24}
                                color="red"
                                onClick={() =>
                                  removeQuantity(
                                    item.id_product,
                                    item?.quantity
                                  )
                                }
                              />
                            </button>
                          </td>
                        </tr>
                      ))}
                      <tr>
                        <td>Customer Name</td>
                        <td>{cart?.customerName}</td>
                      </tr>
                      <tr>
                        <td>Total</td>
                        <td>{numberToIDR(cart?.totalPrice)}</td>
                      </tr>
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
            </div>
          </section>

          {isSuccessAddOrder ? (
            <button
              className="btn btn-outline btn-primary w-full"
              type="button"
              onClick={() => {
                if (redirectURL) {
                  window.location.href = redirectURL;
                } else {
                  alert("Redirect URL tidak ditemukan!");
                }
              }}>
              PAY
            </button>
          ) : (
            <button
              className="btn btn-outline btn-primary w-full"
              type="submit">
              Submit
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
