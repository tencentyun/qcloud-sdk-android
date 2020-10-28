/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.constraints;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.work.NetworkType;

import com.tencent.cos.xml.transfer.TransferState;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TransferSpecDao {


    @Insert(onConflict = REPLACE)
    void insertTransferSpec(TransferSpec transferSpec);

    @Query("SELECT * FROM transferspec")
    LiveData<List<TransferSpec>> getAllTransferSpecsLiveData();

    @Query("SELECT * FROM transferspec")
    List<TransferSpec> getAllTransferSpecs();

    @Query("SELECT * FROM transferspec WHERE id IN (:ids)")
    List<TransferSpec> getTransferSpecs(List<String> ids);

    @Query("SELECT state FROM transferspec WHERE id=:id")
    LiveData<TransferState> getTransferSpecsStateLiveData(String id);

    @Query("SELECT * FROM transferspec WHERE id=:id")
    TransferSpec getTransferSpecs(String id);

    @Query("SELECT * FROM transferspec WHERE bucket=:bucket AND `key`=:cosKey AND filePath=:filePath")
    TransferSpec getTransferSpecByEndpoint(String bucket, String cosKey, String filePath);

    @Query("SELECT * FROM transferspec WHERE id=:id")
    LiveData<TransferSpec> getTransferSpecsLiveData(String id);

    @Query("UPDATE transferspec SET workerId=:workerId WHERE id=:id")
    void updateTransferSpecWorkerId(String id, String workerId);

    @Query("UPDATE transferspec SET transferServiceEgg=:serviceEgg WHERE id=:id")
    void updateTransferSpecServiceEgg(String id, String serviceEgg);

    @Query("UPDATE transferspec SET complete=:complete AND target=:target WHERE id=:id")
    void updateTransferSpecTransferProgress(String id, long complete, long target);

    @Query("UPDATE transferspec SET state=:state WHERE id=:id")
    void updateTransferSpecTransferStatus(String id, TransferState state);

    @Query("UPDATE transferspec SET uploadId=:uploadId WHERE id=:id")
    void updateTransferSpecUploadId(String id, String uploadId);

    @Query("UPDATE transferspec SET complete=:complete AND target=:target WHERE id=:id")
    void updateTransferSpecProgress(String id, long complete, long target);

    @Query("UPDATE transferspec SET required_network_type=:networkType WHERE id=:id")
    void updateTransferSpecNetworkConstraints(String id, NetworkType networkType);

    @Query("UPDATE transferspec SET required_network_type=:networkType WHERE id IN (:ids)")
    void updateTransferSpecsNetworkConstraints(List<String> ids, NetworkType networkType);

    @Query("UPDATE transferspec SET required_network_type=:networkType")
    void updateAllTransferSpecsNetworkConstraints(NetworkType networkType);

    @Query("DELETE FROM transferspec WHERE id=:id")
    void deleteTransferSpec(String id);
}
