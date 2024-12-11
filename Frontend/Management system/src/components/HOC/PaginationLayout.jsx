import Container from "../container";
import Header from "../../layouts/partials/header";
import { Pagination } from "@nextui-org/pagination";

const PaginationLayout = ({
  dispatch,
  children,
  headerTitle,
  pagingData = [],
  paging = { totalPages: 0, totalElements: 0 },
  page,
  setPage = () => {},
  headerRenderAction = () => {},
}) => {
  const handlePageChange = (newPage) => {
    dispatch(setPage(newPage));
  };

  return (
    <Container>
      <Header title={headerTitle} />
      {headerRenderAction}
      {children}
      {pagingData &&
        pagingData.length > 0 &&
        paging &&
        paging.totalPages > 1 && (
          <div className="my-6">
            <Pagination
              initialPage={page}
              total={paging?.totalPages}
              onChange={handlePageChange}
            />
          </div>
        )}
    </Container>
  );
};

export default PaginationLayout;
