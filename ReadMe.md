@Transactional(readOnly=true) makes transaction to not commit changes to @Entity without explicitly saving it, so
@Transactional(ReadOnly=false)
public void Trx(){
Entity entity = rep.findById(1).orElseThrow;
entity.setName("newName");
//with no rep.save
!!!It will still save @Entity - THAT IS AN OPTIMISATION THAT readOnly=true DOES!!!!
}