package service.product;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.product.Comment;
import model.product.Product;

public interface ProductService {
	public List getProductList(Product board);
	
    public Product getProductById(String boardId);
    
    public List getMyProductList(Product board);
    
    public boolean createProduct(Product board, HttpServletRequest request);
    
    public boolean updateProduct(Product board, HttpServletRequest request);
    
    public boolean deleteProduct(Product board);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);
    
    public boolean updateProductLike(Product board);
    
}
