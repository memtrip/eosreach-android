#include <eosiolib/eosio.hpp>
#include <eosiolib/print.hpp>
#include <string>

using namespace eosio;
using std::string;

class memtripissue : public contract {
public:
  memtripissue(account_name self) : contract(self) {}

  // @abi action
  void receipt(
    const account_name account,
    const checksum256& uniqueId
  );

private:
  // @abi table purchases i64
  struct purchase {
    account_name  account;
    checksum256   uniqueId;

    account_name primary_key() const { return account; }
    key256 by_unique_id() const { return get_unique_id(uniqueId); }

    static key256 get_unique_id(const checksum256& value) {
       const uint64_t *p64 = reinterpret_cast<const uint64_t *>(&value);
       return key256::make_from_word_sequence<uint64_t>(p64[0], p64[1], p64[2], p64[3]);
    }

    EOSLIB_SERIALIZE(purchase, (account)(uniqueId))
  };

  typedef eosio::multi_index<N(purchases), purchase,
     indexed_by<N(uniqueId), const_mem_fun<purchase, key256, &purchase::by_unique_id>
     >
  > purchase_table;
};

EOSIO_ABI(memtripissue, (receipt))
