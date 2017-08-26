package life.rnl.migration.batch.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.destination.domain.Part;
import life.rnl.migration.destination.repository.PartRepository;
import life.rnl.migration.source.domain.Asset;

public class AssetItemProcessor implements ItemProcessor<Asset, Item> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private PartRepository partRepository;

	private Integer count = 0;

	@Override
	public Item process(Asset item) throws Exception {
		Integer currentCount = count++;
		log.info("[START] Processing record # {}", currentCount);

		Item newItem = new Item();
		newItem.setAssetId(item.getId());
		newItem.setDateCreated(item.getDateCreated());
		newItem.setSerialNumber(item.getSerialNumber());
		
		Part part = partRepository.findOne(item.getPartType().getId());
		newItem.setPart(part);

		log.info("[END] Processed record # {}", currentCount);
		return newItem;
	}

	public PartRepository getPartRepository() {
		return partRepository;
	}

	public void setPartRepository(PartRepository partRepository) {
		this.partRepository = partRepository;
	}
}