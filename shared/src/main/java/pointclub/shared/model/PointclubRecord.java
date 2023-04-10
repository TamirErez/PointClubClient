package pointclub.shared.model;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointclubRecord extends SugarRecord implements Serializable {
   private int serverId;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PointclubRecord that = (PointclubRecord) o;

      return serverId == that.serverId;
   }
}
