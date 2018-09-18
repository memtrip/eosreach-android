#include <memtripissue.hpp>
#include <eosiolib/crypto.h>

void memtripissue::receipt(
  const account_name account,
  const checksum256& uniqueId
) {
  require_auth(account);

  purchase_table purchase(_self, _self);

  auto idx = purchase.get_index<N(uniqueId)>();
  auto itr = idx.find(purchase::get_unique_id(uniqueId));

  eosio_assert(itr == idx.end(), "PurchaseId already exists.");

  purchase.emplace(account, [&](auto& p) {
    p.account = account;
    p.uniqueId = uniqueId;
  });
}
