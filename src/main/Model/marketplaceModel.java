import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Table(name = tbl_marketplace)
public class marketplaceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = Generation.Type.AUTO)

    private UUID productId;
    private String name;
    private BigDecimal value;

    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    
}
